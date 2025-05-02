package com.millo.ollim.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.user.domain.UserRole
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException
import java.time.Duration

/**
 * OAuth2 인증 성공 시 JWT 발급 및 Redis에 RefreshToken 저장 처리
 * - AccessToken은 JWT로 바로 응답
 * - RefreshToken은 JWT 형식으로 발급하여 Redis에 저장
 * - Redis key 전략은 JwtTokenProvider에서 위임 관리
 */
@Component
class OAuth2SuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate: StringRedisTemplate,
    private val objectMapper: ObjectMapper = ObjectMapper()
) : AuthenticationSuccessHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Throws(IOException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val principal = authentication.principal as UserPrincipal
        val userId = principal.userId
        val role = UserRole.valueOf(principal.role)

        // AccessToken 발급
        val accessToken = jwtTokenProvider.generateAccessToken(
            userId = userId,
            role = role,
            email = principal.email,
            nickname = principal.nickname
        )

        // RefreshToken 발급
        val refreshToken = jwtTokenProvider.generateRefreshToken(userId)

        // Redis 저장 (key 위임)
        val redisKey = jwtTokenProvider.getRefreshTokenKey(userId)
        redisTemplate.opsForValue().set(redisKey, refreshToken, Duration.ofDays(14))

        log.info(">>> [OAuth2 인증 성공] userId=$userId, refreshToken 저장 완료")

        // 최소한의 JSON 응답 반환
        val tokenResponse = mapOf(
            "accessToken" to accessToken,
            "refreshToken" to refreshToken
        )

        response.status = HttpServletResponse.SC_OK
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.setHeader("Authorization", "Bearer $accessToken")
        response.writer.write(objectMapper.writeValueAsString(tokenResponse))
    }
}
