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
 * RefreshTokenService Interface 구현체
 * - Redis에 저장된 RefreshToken을 검증 후 AccessToken + 새로운 RefreshToken 발급
 */
@Service
class RefreshTokenServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate: StringRedisTemplate,
    private val userRepository: UserRepository
) : RefreshTokenService {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun reissueAccessToken(request: TokenDTO.Request): TokenDTO.Response {
        log.info(">>> [AccessToken 재발급 요청 수신]")

        // 입력 검증
        require(request.refreshToken.isNotBlank()) { "refreshToken은 필수입니다." }

        // RefreshToken 파싱 및 userId 추출
        val userId: UUID = try {
            jwtTokenProvider.getUserIdFromRefreshToken(request.refreshToken)
        } catch (e: Exception) {
            log.warn(">>> [RefreshToken 파싱 실패] ${e.message}")
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.")
        }

        // Redis 키 생성
        val redisKey = jwtTokenProvider.getRefreshTokenKey(userId)
        val storedRefreshToken = redisTemplate.opsForValue().get(redisKey)

        if (storedRefreshToken.isNullOrBlank()) {
            log.warn(">>> [Redis 저장된 RefreshToken 없음] key=$redisKey")
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 정보가 만료되었습니다.")
        }

        if (storedRefreshToken != request.refreshToken) {
            log.warn(">>> [RefreshToken 불일치] 저장된 값과 다름")
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.")
        }

        // 사용자 조회
        val user = userRepository.findById(userId)
            .orElseThrow {
                log.warn(">>> [사용자 없음] userId=$userId")
                ResponseStatusException(HttpStatus.UNAUTHORIZED, "존재하지 않는 사용자입니다.")
            }

        // AccessToken + 새 RefreshToken 생성
        val newAccessToken = jwtTokenProvider.generateAccessToken(
            userId = user.id,
            role = user.role,
            email = user.email,
            nickname = user.profile?.nickname ?: "익명"
        )

        val newRefreshToken = jwtTokenProvider.generateRefreshToken(user.id)

        // Redis에 RefreshToken 교체 (Rotation)
        redisTemplate.opsForValue().set(redisKey, newRefreshToken, Duration.ofDays(14))

        log.info(">>> [AccessToken + RefreshToken 재발급 완료] userId=$userId")

        return TokenDTO.Response(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }
}
