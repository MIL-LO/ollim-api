package com.millo.ollim.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.millo.ollim.auth.domain.UserPrincipal
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException
import java.time.Duration

/**
 * OAuth2 인증 성공 시 JWT 발급 및 Redis에 RefreshToken 저장 처리
 *
 * 모바일(PWA 포함) 서비스에 맞춰 다음과 같은 정책을 적용합니다:
 * - AccessToken/RefreshToken은 JSON body로 응답
 * - Authorization 헤더는 사용하지 않음 (모바일 앱에서 별도 저장)
 * - 사용자 상태(status: PENDING, ACTIVE 등)를 함께 전달해 클라이언트가 처리 분기 가능
 */
@Component
class OAuth2SuccessHandler(
    @Value("\${app.frontend-url}")
    private val frontendUrl: String,
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
        val role = principal.role
        val status = principal.status
        val email = principal.email
        val nickname = principal.nickname

        // AccessToken 발급
        val accessToken = jwtTokenProvider.generateAccessToken(
            userId = userId,
            role = role,
            status = status,
            email = email,
            nickname = nickname
        )

        // RefreshToken 발급
        val refreshToken = jwtTokenProvider.generateRefreshToken(userId)

        // Redis에 RefreshToken 저장
        val redisKey = jwtTokenProvider.getRefreshTokenKey(userId)
        redisTemplate.opsForValue().set(redisKey, refreshToken, Duration.ofDays(14))

        log.info(">>> [OAuth2 인증 성공] userId=$userId, status=$status, refreshToken 저장 완료")

        // 토큰 정보를 URL 파라미터로 전달하며 프론트엔드로 리디렉션
        response.sendRedirect(
            "$frontendUrl/auth/callback?" +
            "accessToken=$accessToken&" +
            "refreshToken=$refreshToken&" +
            "status=${status.name}"
        )
    }
}
