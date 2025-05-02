package com.millo.ollim.auth.service

import com.millo.ollim.auth.dto.TokenDTO
import com.millo.ollim.common.util.JwtTokenProvider
import com.millo.ollim.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Duration
import java.util.*

/**
 * RefreshToken 재발급 서비스 구현체
 * - Redis에서 기존 RefreshToken 검증 후
 * - 새로운 AccessToken + RefreshToken 발급
 * - 새로운 RefreshToken으로 Redis 업데이트
 */
@Service
class RefreshTokenServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate: StringRedisTemplate,
    private val userRepository: UserRepository
) : RefreshTokenService {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun reissueAccessToken(tokenRequest: TokenDTO.TokenRequest): TokenDTO.TokenResponse {
        log.info(">>> [재발급 요청] RefreshToken 수신")

        val refreshToken = tokenRequest.refreshToken
        if (refreshToken.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "RefreshToken은 필수입니다.")
        }

        // RefreshToken 형식 검증
        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            log.warn(">>> [형식 오류] 요청된 토큰은 RefreshToken이 아님")
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "형식이 올바르지 않은 RefreshToken입니다.")
        }

        // RefreshToken 검증 및 사용자 ID 추출
        val userId: UUID = try {
            jwtTokenProvider.getUserIdFromRefreshToken(refreshToken)
        } catch (e: Exception) {
            log.warn(">>> [RefreshToken 파싱 실패] ${e.message}")
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.")
        }

        val redisKey = jwtTokenProvider.getRefreshTokenKey(userId)
        val storedRefreshToken = redisTemplate.opsForValue().get(redisKey)

        if (storedRefreshToken.isNullOrBlank()) {
            log.warn(">>> [Redis 저장된 RefreshToken 없음] key=$redisKey")
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 정보가 만료되었습니다.")
        }

        if (storedRefreshToken != refreshToken) {
            log.warn(">>> [RefreshToken 불일치] 저장된 값과 다름")
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.")
        }

        // 사용자 정보 조회
        val user = userRepository.findById(userId)
            .orElseThrow {
                log.warn(">>> [사용자 없음] userId=$userId")
                ResponseStatusException(HttpStatus.UNAUTHORIZED, "존재하지 않는 사용자입니다.")
            }

        // 새로운 AccessToken + RefreshToken 생성
        val newAccessToken = jwtTokenProvider.generateAccessToken(
            userId = user.id,
            role = user.role,
            status = user.status,
            email = user.email,
            nickname = user.profile?.nickname
        )

        val newRefreshToken = jwtTokenProvider.generateRefreshToken(user.id)

        // Redis 갱신
        redisTemplate.opsForValue().set(redisKey, newRefreshToken, Duration.ofDays(14))
        log.info(">>> [재발급 완료] userId=$userId, AccessToken + RefreshToken 재발급")

        return TokenDTO.TokenResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            status = user.status
        )
    }
}
