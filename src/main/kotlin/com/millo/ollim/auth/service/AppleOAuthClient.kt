package com.millo.ollim.auth.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.millo.ollim.auth.dto.OAuthUserInfo
import com.millo.ollim.user.domain.ProviderType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Apple OAuth 연동을 위한 외부 API 호출 클라이언트
 */
@Component
class AppleOAuthClient(
    private val jwtGenerator: AppleJwtGenerator,
    @Value("\${oauth.apple.client-id}") private val clientId: String,
    @Value("\${oauth.apple.team-id}") private val teamId: String,
    @Value("\${oauth.apple.key-id}") private val keyId: String,
    @Value("\${oauth.apple.token-url}") private val tokenUrl: String
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    private val objectMapper = ObjectMapper()

    /**
     * 인증 코드로 access token 요청
     */
    fun getAccessToken(code: String): String {
        val clientSecret = jwtGenerator.createClientSecret(
            clientId = clientId,
            teamId = teamId,
            keyId = keyId
        )

        log.info("Apple OAuth 토큰 요청 시작")

        val url = URL(tokenUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        connection.doOutput = true

        val body = "client_id=$clientId&client_secret=$clientSecret&code=$code&grant_type=authorization_code"
        connection.outputStream.write(body.toByteArray())

        val response = InputStreamReader(connection.inputStream).readText()

        val tokenResponse = objectMapper.readValue(response, AppleTokenResponse::class.java)
        return tokenResponse.idToken
    }

    /**
     * ID Token 기반 사용자 정보 파싱
     */
    fun getUserInfo(idToken: String): OAuthUserInfo {
        val claims = jwtGenerator.decodeIdToken(idToken)

        // Apple의 사용자 정보를 OAuthUserInfo로 변환
        return OAuthUserInfo(
            email = claims["email"] as String,
            name = claims["name"] as? String,
            provider = ProviderType.APPLE,
            oauthId = claims["sub"] as String
        )
    }

    /**
     * Apple 토큰 응답 DTO
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AppleTokenResponse(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("expires_in") val expiresIn: Int,
        @JsonProperty("id_token") val idToken: String,
        @JsonProperty("refresh_token") val refreshToken: String
    )
}
