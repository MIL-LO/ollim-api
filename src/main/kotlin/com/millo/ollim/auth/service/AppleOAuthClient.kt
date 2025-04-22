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
 * Apple OAuth 연동을 위한 외부 API 호출 클라이언트
 */
@Component
class AppleOAuthClient(
    private val webClient: WebClient,
    private val jwtGenerator: AppleJwtGenerator,
    @Value("\${oauth.apple.client-id}") private val clientId: String,
    @Value("\${oauth.apple.team-id}") private val teamId: String,
    @Value("\${oauth.apple.key-id}") private val keyId: String,
    @Value("\${oauth.apple.token-url}") private val tokenUrl: String,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

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

        val response = webClient.post()
            .uri(tokenUrl)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .body(
                BodyInserters.fromFormData("client_id", clientId)
                    .with("client_secret", clientSecret)
                    .with("code", code)
                    .with("grant_type", "authorization_code")
            )
            .retrieve()
            .onStatus({ it.isError }) { clientResponse ->
                clientResponse.bodyToMono(String::class.java).flatMap { errorBody ->
                    log.error("Apple 토큰 요청 실패: $errorBody")
                    Mono.error(IllegalStateException("Apple 토큰 요청 실패: $errorBody"))
                }
            }
            .bodyToMono(AppleTokenResponse::class.java)
            .block() ?: throw IllegalStateException("Apple 토큰 응답이 null입니다.")

        return response.idToken
    }

    /**
     * ID Token 기반 사용자 정보 파싱
     */
    fun getUserInfo(idToken: String): OAuthUserInfo {
        val claims = jwtGenerator.decodeIdToken(idToken)
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
    data class AppleTokenResponse(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("expires_in") val expiresIn: Int,
        @JsonProperty("id_token") val idToken: String,
        @JsonProperty("refresh_token") val refreshToken: String
    )
}
