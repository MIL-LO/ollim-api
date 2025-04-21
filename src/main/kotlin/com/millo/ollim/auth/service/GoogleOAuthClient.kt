package com.millo.ollim.auth.service

import com.fasterxml.jackson.annotation.JsonProperty
import com.millo.ollim.auth.dto.OAuthUserInfo
import com.millo.ollim.user.domain.ProviderType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

/**
 * Google OAuth 연동을 위한 외부 API 호출 클라이언트
 */
@Component
class GoogleOAuthClient(
    private val webClient: WebClient,
    @Value("\${oauth.google.client-id}") private val clientId: String,
    @Value("\${oauth.google.client-secret}") private val clientSecret: String,
    @Value("\${oauth.google.redirect-uri}") private val redirectUri: String,
    @Value("\${oauth.google.token-url}") private val tokenUrl: String,
    @Value("\${oauth.google.user-info-url}") private val userInfoUrl: String
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Google OAuth 인증 코드로 access token 획득
     */
fun getAccessToken(code: String): String {
    log.info("Token 요청: code={}, redirect_uri={}, client_id={}, client_secret={}", code, redirectUri, clientId, clientSecret)
    log.info("Google OAuth 토큰 요청 시작")
    log.debug("code=$code, redirectUri=$redirectUri")

    val response = webClient.post()
        .uri(tokenUrl)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .body(
            BodyInserters.fromFormData("code", code)
                .with("client_id", clientId)
                .with("client_secret", clientSecret)
                .with("redirect_uri", redirectUri)
                .with("grant_type", "authorization_code")
        )
        .retrieve()
        .onStatus({ it.isError }) { clientResponse ->
            clientResponse.bodyToMono(String::class.java).flatMap { errorBody ->
                log.error("Google 토큰 요청 실패: $errorBody")
                Mono.error(IllegalStateException("Google 토큰 요청 실패: $errorBody"))
            }
        }
        .bodyToMono(GoogleTokenResponse::class.java)
        .block() ?: throw IllegalStateException("Google 토큰 응답이 null입니다.")

    return response.accessToken // ✅ accessToken 필드만 반환
}

    /**
     * access token으로 Google 사용자 정보 조회
     */
    fun getUserInfo(accessToken: String): OAuthUserInfo {
        log.info("Google 사용자 정보 조회 시작")

        val response = webClient.get()
            .uri(userInfoUrl)
            .headers { it.setBearerAuth(accessToken) }
            .retrieve()
            .onStatus({ it.isError }) { clientResponse ->
                clientResponse.bodyToMono(String::class.java).flatMap { errorBody ->
                    log.error("Google 사용자 정보 요청 실패: $errorBody")
                    Mono.error(IllegalStateException("Google 사용자 정보 요청 실패: $errorBody"))
                }
            }
            .bodyToMono(GoogleUserResponse::class.java)
            .block() ?: throw IllegalStateException("Google 사용자 정보 응답이 null입니다.")

        return OAuthUserInfo(
            email = response.email,
            name = response.name,
            provider = ProviderType.GOOGLE,
            oauthId = response.id
        )
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
    data class GoogleUserResponse(
        val id: String,
        val email: String,
        val name: String
    )
}
