package com.millo.ollim.auth.service

import OAuthUserInfo
import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.auth.dto.AuthInfoDTO
import com.millo.ollim.auth.dto.TokenDTO
import com.millo.ollim.common.util.JwtTokenProvider
import com.millo.ollim.common.util.TokenExtractor
import com.millo.ollim.user.domain.*
import com.millo.ollim.user.dto.UserProfileDTO
import com.millo.ollim.user.repository.UserOAuthRepository
import com.millo.ollim.user.repository.UserProfileRepository
import com.millo.ollim.user.repository.UserRepository
import com.millo.ollim.user.service.NicknameService
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

/**
 * 인증 관련 비즈니스 로직을 담당하는 서비스 구현체
 */
@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val userOAuthRepository: UserOAuthRepository,
    private val userProfileRepository: UserProfileRepository,
    private val redisTemplate: StringRedisTemplate,
    private val jwtTokenProvider: JwtTokenProvider,
    private val nicknameService: NicknameService
) : AuthService {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * OAuth 로그인 성공 시 사용자 등록 또는 로그인 시간 갱신
     */
    @Transactional
    override fun saveOrUpdateUser(userInfo: OAuthUserInfo): UserEntity {
        val providerType = ProviderType.valueOf(userInfo.getProvider().uppercase())
        val existingUser = userRepository.findByEmail(userInfo.getEmail())

        return if (existingUser != null) {
            existingUser.lastLoginAt = LocalDateTime.now()
            userRepository.save(existingUser)
        } else {
            val newUser = UserEntity(
                email = userInfo.getEmail(),
                role = UserRole.USER,
                status = UserStatus.PENDING,
                lastLoginAt = LocalDateTime.now()
            )

            val profile = UserProfileEntity(
                userId = newUser.id,
                user = newUser,
                nickname = nicknameService.generateNickname().nickname
            )
            newUser.profile = profile

            userRepository.save(newUser)
            userOAuthRepository.save(
                UserOAuthEntity(
                    user = newUser,
                    provider = providerType,
                    oauthId = userInfo.getProviderId(),
                    createdAt = LocalDateTime.now()
                )
            )

            log.info(">>> 신규 사용자 등록 완료: userId=${newUser.id}")
            newUser
        }
    }

    /**
     * 현재 로그인된 사용자 정보를 DTO로 반환
     */
    @Transactional(readOnly = true)
    override fun getAuthInfo(user: UserPrincipal): AuthInfoDTO.AuthInfoResponse {
        return AuthInfoDTO.AuthInfoResponse(
            userId = user.userId,
            email = user.email,
            role = user.role,
            status = user.status,
            nickname = user.nickname
        )
    }

    /**
     * 회원가입 시 프로필 저장 및 상태 ACTIVE로 전환
     */
    @Transactional
    override fun registerUserProfile(userId: UUID, userProfileRequest: UserProfileDTO.UserProfileRequest) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다. id=$userId") }

        val profile = user.profile?.apply {
            nickname = userProfileRequest.nickname
            gender = userProfileRequest.gender
            birthDate = userProfileRequest.birthDate
            energyType = userProfileRequest.energyType
            activeTime = userProfileRequest.activeTime
            activitySpaces = userProfileRequest.activitySpaces
            mbti = userProfileRequest.mbti
            profileImage = userProfileRequest.profileImage
        } ?: UserProfileEntity(
            userId = userId,
            user = user,
            nickname = userProfileRequest.nickname,
            gender = userProfileRequest.gender,
            birthDate = userProfileRequest.birthDate,
            energyType = userProfileRequest.energyType,
            activeTime = userProfileRequest.activeTime,
            activitySpaces = userProfileRequest.activitySpaces,
            mbti = userProfileRequest.mbti,
            profileImage = userProfileRequest.profileImage
        )

        user.profile = profile
        user.status = UserStatus.ACTIVE

        userProfileRepository.save(profile)
        userRepository.save(user)

        log.info(">>> 회원가입 프로필 저장 및 상태 ACTIVE 전환 완료: userId=$userId")
    }

    /**
     * RefreshToken을 검증하고 새로운 AccessToken + RefreshToken을 발급
     */
    @Transactional
    override fun refresh(tokenRequest: TokenDTO.TokenRequest): TokenDTO.TokenResponse {
        val refreshToken = tokenRequest.refreshToken

        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw IllegalArgumentException("유효하지 않은 RefreshToken입니다.")
        }

        val userId = jwtTokenProvider.getUserIdFromRefreshToken(refreshToken)

        val redisKey = jwtTokenProvider.getRefreshTokenKey(userId)
        val storedToken = redisTemplate.opsForValue().get(redisKey)
            ?: throw IllegalArgumentException("Redis에서 RefreshToken을 찾을 수 없습니다.")

        if (storedToken != refreshToken) {
            throw IllegalArgumentException("RefreshToken이 일치하지 않습니다.")
        }

        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다. id=$userId") }

        val accessToken = jwtTokenProvider.generateAccessToken(
            userId = user.id,
            role = user.role,
            status = user.status,
            email = user.email,
            nickname = user.profile?.nickname
        )

        val newRefreshToken = jwtTokenProvider.generateRefreshToken(user.id)
        redisTemplate.opsForValue().set(redisKey, newRefreshToken, Duration.ofDays(14))

        return TokenDTO.TokenResponse(
            accessToken = accessToken,
            refreshToken = newRefreshToken,
            status = user.status
        )
    }

    /**
     * 로그아웃 요청 처리 (Request에서 AccessToken 추출)
     */
    override fun logoutWithRequest(user: UserPrincipal, request: HttpServletRequest) {
        val accessToken = TokenExtractor.extractAccessToken(request)
            ?: throw IllegalArgumentException("AccessToken이 필요합니다.")
        logout(user, accessToken)
    }

    /**
     * Redis에서 RefreshToken 제거 + AccessToken 블랙리스트 등록
     */
    override fun logout(user: UserPrincipal, accessToken: String) {
        val refreshTokenKey = jwtTokenProvider.getRefreshTokenKey(user.userId)
        redisTemplate.delete(refreshTokenKey)
        log.info(">>> [로그아웃] RefreshToken 삭제 완료: userId=${user.userId}")

        val tokenExpiry = jwtTokenProvider.getExpiration(accessToken)
        val ttlMillis = tokenExpiry.time - System.currentTimeMillis()

        if (ttlMillis > 0) {
            val blacklistKey = jwtTokenProvider.getBlacklistKey(accessToken)
            redisTemplate.opsForValue().set(blacklistKey, "logout", Duration.ofMillis(ttlMillis))
            log.info(">>> [로그아웃] AccessToken 블랙리스트 등록 완료: TTL=${ttlMillis}ms")
        } else {
            log.warn(">>> [로그아웃] 이미 만료된 AccessToken (블랙리스트 등록 생략)")
        }
    }
}
