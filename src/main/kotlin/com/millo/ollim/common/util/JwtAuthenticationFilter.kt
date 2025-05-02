package com.millo.ollim.common.util

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

/**
 * JWT AccessToken을 검증하고 SecurityContext에 인증 정보를 설정하는 필터
 * - 블랙리스트 처리된 토큰인지도 검사
 */
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate: StringRedisTemplate
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            // Authorization 헤더에서 토큰 추출
            val accessToken = TokenExtractor.extractAccessToken(request)

            if (accessToken != null) {
                log.debug("JWT 토큰 추출됨: $accessToken")

                // 블랙리스트 확인
                if (isBlacklisted(accessToken)) {
                    log.warn("블랙리스트에 등록된 토큰입니다. 거부합니다.")
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 세션입니다. 다시 로그인해주세요.")
                    return
                }

                if (jwtTokenProvider.validateToken(accessToken)) {
                    val authentication = jwtTokenProvider.getAuthentication(accessToken)
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
     * Redis에 저장된 블랙리스트 토큰인지 확인
     */
    private fun isBlacklisted(token: String): Boolean {
        val blacklistKey = jwtTokenProvider.getBlacklistKey(token)
        return redisTemplate.hasKey(blacklistKey) == true
    }
}
