package com.millo.ollim.auth.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.millo.ollim.auth.dto.OAuthUserInfo
import com.millo.ollim.user.domain.ProviderType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Google OAuth 연동을 위한 외부 API 호출 클라이언트
 */
@Component
class GoogleOAuthClient(
    @Value("\${oauth.google.client-id}") private val clientId: String,
    @Value("\${oauth.google.client-secret}") private val clientSecret: String,
    @Value("\${oauth.google.redirect-uri}") private val redirectUri: String,
    @Value("\${oauth.google.token-url}") private val tokenUrl: String,
    @Value("\${oauth.google.user-info-url}") private val userInfoUrl: String
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val objectMapper = ObjectMapper()

    /**
     * Google OAuth 인증 코드로 access token 획득
     */
    fun getAccessToken(code: String): String {
        log.info("Google OAuth 토큰 요청 시작")

        val body = "code=$code&client_id=$clientId&client_secret=$clientSecret&redirect_uri=$redirectUri&grant_type=authorization_code"
        val response = sendHttpRequest(tokenUrl, "POST", body)

        val tokenResponse = objectMapper.readValue(response, GoogleTokenResponse::class.java)
        return tokenResponse.accessToken
    }

    /**
     * access token으로 Google 사용자 정보 조회
     */
    fun getUserInfo(accessToken: String): OAuthUserInfo {
        log.info("Google 사용자 정보 조회 시작")

        val response = sendHttpRequest(userInfoUrl, "GET", "", accessToken)

        val userResponse = objectMapper.readValue(response, GoogleUserResponse::class.java)

        // OAuthUserInfo로 변환 후 반환
        return OAuthUserInfo(
            email = userResponse.email,
            name = userResponse.name,
            provider = ProviderType.GOOGLE,
            oauthId = userResponse.id
        )
    }

    /**
     * 공통 HTTP 요청 처리 함수
     */
    private fun sendHttpRequest(url: String, method: String, body: String, accessToken: String? = null): String {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = method
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            accessToken?.let { setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer $it") }
            doOutput = method == "POST"
        }

        try {
            // 요청 본문 작성
            if (method == "POST") {
                connection.outputStream.write(body.toByteArray())
            }

            // 응답 받기
            val response = InputStreamReader(connection.inputStream).readText()
            connection.disconnect()
            return response
        } catch (e: Exception) {
            connection.disconnect()
            throw RuntimeException("HTTP 요청 실패: $e")
        }
    }

    /**
     * Google 토큰 응답 DTO
     */
    data class GoogleTokenResponse(
        @JsonProperty("access_token")
        val accessToken: String,

        @JsonProperty("expires_in")
        val expiresIn: Int,

        @JsonProperty("refresh_token")
        val refreshToken: String?,

        @JsonProperty("scope")
        val scope: String,

        @JsonProperty("token_type")
        val tokenType: String,

        @JsonProperty("id_token")
        val idToken: String?
    )

    /**
     * Google 사용자 정보 응답 DTO
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class GoogleUserResponse(
        @JsonProperty("id")
        val id: String,
        @JsonProperty("email")
        val email: String,
        @JsonProperty("name")
        val name: String,
    )
}
