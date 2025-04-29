package com.millo.ollim.common.util

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory

/**
 * 요청 헤더에서 AccessToken(Bearer)만 추출하는 유틸
 */
object TokenExtractor {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Authorization 헤더에서 Bearer 토큰 추출
     *
     * @param request HttpServletRequest
     * @return Bearer AccessToken (prefix 제거된 값), 없으면 null
     */
    fun extractAccessToken(request: HttpServletRequest): String? {
        val rawHeader = request.getHeader("Authorization")
        log.debug("요청 Authorization 헤더: $rawHeader")

        return if (!rawHeader.isNullOrBlank() && rawHeader.startsWith("Bearer ")) {
            rawHeader.removePrefix("Bearer ").trim()
        } else {
            null
        }
    }
}
