package com.millo.ollim.common.util

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

/**
 * JWT AccessToken을 검증하고 SecurityContext에 인증 정보를 설정하는 필터
 */
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = resolveToken(request)

            if (token != null) {
                log.debug("JWT 토큰 추출됨: $token")

                if (jwtTokenProvider.validateToken(token)) {
                    val authentication = jwtTokenProvider.getAuthentication(token)
                    authentication?.let {
                        log.debug("인증 객체 설정 완료: ${it.name}")
                        SecurityContextHolder.getContext().authentication = it
                    }
                } else {
                    log.warn("유효하지 않은 JWT 토큰")
                }
            } else {
                log.debug("Authorization 헤더에 JWT 토큰 없음")
            }

        } catch (ex: Exception) {
            log.error("JWT 필터 처리 중 예외 발생: ${ex.message}", ex)
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }

    /**
     * Authorization 헤더에서 Bearer 토큰 추출
     */
    private fun resolveToken(request: HttpServletRequest): String? {
        val rawHeader = request.getHeader("Authorization")
        log.debug("요청 Authorization 헤더: $rawHeader")

        return if (!rawHeader.isNullOrBlank() && rawHeader.startsWith("Bearer ")) {
            rawHeader.removePrefix("Bearer").trim()
        } else {
            null
        }
    }
}
