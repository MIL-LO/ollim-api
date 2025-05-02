package com.millo.ollim.auth.service

import OAuthUserInfo
import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.common.util.JwtTokenProvider
import com.millo.ollim.user.domain.*
import com.millo.ollim.user.repository.UserOAuthRepository
import com.millo.ollim.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

/**
 * 인증 관련 Service 구현체
 */
@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val userOAuthRepository: UserOAuthRepository,
    private val redisTemplate: StringRedisTemplate,
    private val jwtTokenProvider: JwtTokenProvider
) : AuthService {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * OAuth2 로그인 성공 시 사용자 저장 또는 업데이트
     */
    @Transactional
    override fun saveOrUpdateUser(userInfo: OAuthUserInfo): UserEntity {
        val providerType = ProviderType.valueOf(userInfo.getProvider().uppercase())

        log.info(">>> 사용자 이메일로 회원 존재 여부 확인 시작: email={}", userInfo.getEmail())
        val existingUser = userRepository.findByEmail(userInfo.getEmail())

        if (existingUser != null) {
            existingUser.lastLoginAt = LocalDateTime.now()
            return userRepository.save(existingUser)
        }

        val newUser = UserEntity(
            email = userInfo.getEmail(),
            role = UserRole.USER,
            status = UserStatus.ACTIVE,
            lastLoginAt = LocalDateTime.now()
        )

        val profile = UserProfileEntity(
            userId = newUser.id,
            user = newUser,
            nickname = generateDefaultNickname(userInfo)
        )
        newUser.profile = profile

        userRepository.save(newUser)
        log.info(">>> 신규 사용자 및 프로필 저장 완료: {}", newUser.id)

        userOAuthRepository.save(
            UserOAuthEntity(
                user = newUser,
                provider = providerType,
                oauthId = userInfo.getProviderId(),
                createdAt = LocalDateTime.now()
            )
        )
        log.info(">>> OAuth 연동 저장 완료")

        return newUser
    }

    /**
     * 로그아웃 처리
     * - RefreshToken 삭제
     * - AccessToken 블랙리스트 등록
     */
    override fun logout(user: UserPrincipal, accessToken: String) {
        // Redis에서 RefreshToken 삭제
        val refreshTokenKey = jwtTokenProvider.getRefreshTokenKey(user.userId)
        redisTemplate.delete(refreshTokenKey)
        log.info(">>> [로그아웃] RefreshToken 삭제 완료: userId=${user.userId}")

        // AccessToken을 블랙리스트에 등록
        val tokenExpiry = jwtTokenProvider.getExpiration(accessToken)
        val now = System.currentTimeMillis()
        val ttlMillis = tokenExpiry.time - now

        if (ttlMillis > 0) {
            val blacklistKey = jwtTokenProvider.getBlacklistKey(accessToken)
            redisTemplate.opsForValue().set(blacklistKey, "logout", Duration.ofMillis(ttlMillis))
            log.info(">>> [로그아웃] AccessToken 블랙리스트 등록 완료: TTL=${ttlMillis}ms")
        } else {
            log.warn(">>> [로그아웃] 이미 만료된 AccessToken (블랙리스트 등록 생략)")
        }
    }

    /**
     * 기본 닉네임 생성
     */
    private fun generateDefaultNickname(userInfo: OAuthUserInfo): String {
        val emailPart = userInfo.getEmail().substringBefore("@")
        return "${userInfo.getProvider().lowercase()}_$emailPart"
    }
}
