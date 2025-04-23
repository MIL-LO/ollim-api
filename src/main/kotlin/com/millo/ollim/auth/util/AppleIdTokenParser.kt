package com.millo.ollim.auth.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.millo.ollim.auth.dto.AppleIdTokenPayload
import org.springframework.stereotype.Component
import java.util.*

/**
 * Apple OAuth2에서 받은 id_token을 디코딩 및 파싱하여 사용자 정보를 추출하는 유틸 클래스입니다.
 */
@Component
class AppleIdTokenParser(
    private val objectMapper: ObjectMapper
) {

    fun parse(idToken: String): AppleIdTokenPayload {
        val parts = idToken.split(".")
        if (parts.size != 3) {
            throw IllegalArgumentException("Invalid JWT format")
        }

        val payload = String(Base64.getUrlDecoder().decode(parts[1]))
        return objectMapper.readValue(payload, AppleIdTokenPayload::class.java)
    }
}
