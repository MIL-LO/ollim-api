package com.millo.ollim.common.util

import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.auth.util.JwtTokenProvider
import com.millo.ollim.user.domain.UserRole
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException
import java.time.Duration
import java.util.*

/**
 * OAuth2 인증 성공 시 JWT 발급 및 Redis에 Refresh Token 저장 처리
 */
@Component
class OAuth2SuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate: StringRedisTemplate
) : AuthenticationSuccessHandler {

    @Throws(IOException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val principal = authentication.principal as UserPrincipal
        val userId = principal.userId
        val role = UserRole.valueOf(principal.role)

        // JWT 발급
        val accessToken = jwtTokenProvider.generateAccessToken(userId, role)
        val refreshToken = jwtTokenProvider.generateRefreshToken()

        // Redis에 RefreshToken 저장 (TTL 14일)
        redisTemplate.opsForValue().set(
            "refresh_token:$userId",
            refreshToken,
            Duration.ofDays(14)
        )

        // 클라이언트에 토큰 응답
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(
            """
            {
                "accessToken": "$accessToken",
                "refreshToken": "$refreshToken"
            }
            """.trimIndent()
        )
    }
}
