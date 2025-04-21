package com.millo.ollim.auth.service

import com.millo.ollim.auth.dto.OAuthUserInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

/**
 * Google OAuth 연동을 위한 외부 API 호출용 클라이언트
 * - access token 요청
 * - 사용자 정보 조회
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

    /**
     * Google OAuth 서버에 인증 코드를 전달하여 access token을 획득
     * @param code 인가 코드
     * @return access token 문자열
     */
    fun getAccessToken(code: String): String {
        val response = webClient.post()
            .uri(tokenUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(
                "code=$code&client_id=$clientId&client_secret=$clientSecret&redirect_uri=$redirectUri&grant_type=authorization_code"
            )
            .retrieve()
            .bodyToMono(GoogleTokenResponse::class.java)
            .block() ?: throw IllegalStateException("Google 토큰 요청 실패")

        return response.accessToken
    }

    /**
     * access token을 이용해 사용자 정보를 요청
     * @param accessToken Google에서 발급받은 access token
     * @return OAuthUserInfo 표준 사용자 정보 객체
     */
    fun getUserInfo(accessToken: String): OAuthUserInfo {
        val response = webClient.get()
            .uri(userInfoUrl)
            .headers { it.setBearerAuth(accessToken) }
            .retrieve()
            .bodyToMono(GoogleUserResponse::class.java)
            .block() ?: throw IllegalStateException("Google 사용자 정보 조회 실패")

        return OAuthUserInfo(
            email = response.email,
            name = response.name,
            provider = "GOOGLE",
            oauthId = response.id
        )
    }

    /**
     * Google 토큰 응답 DTO (내부 사용)
     */
    data class GoogleTokenResponse(
        val accessToken: String,
        val expiresIn: Int,
        val refreshToken: String?,
        val scope: String,
        val tokenType: String,
        val idToken: String?
    )

    /**
     * Google 사용자 정보 응답 DTO (내부 사용)
     */
    data class GoogleUserResponse(
        val id: String,
        val email: String,
        val name: String
    )
}
