package com.millo.ollim.auth.service

import com.millo.ollim.auth.util.AppleJwtGenerator
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

/**
 * Apple OAuth2 로그인에서 access token을 요청하는 커스텀 OAuth2AccessTokenResponseClient 구현체
 */
@Component
class AppleOAuthClient(
    private val jwtGenerator: AppleJwtGenerator,
    private val restTemplate: RestTemplate = RestTemplate() // 주입 가능하도록 수정 가능
) : OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * OAuth2 인증 코드로 access_token을 요청하여 OAuth2AccessTokenResponse로 변환합니다.
     */
    override fun getTokenResponse(authorizationGrantRequest: OAuth2AuthorizationCodeGrantRequest): OAuth2AccessTokenResponse {
        val code = authorizationGrantRequest.authorizationExchange.authorizationResponse.code
        val clientRegistration = authorizationGrantRequest.clientRegistration

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val body = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", GRANT_TYPE_AUTHORIZATION_CODE)
            add("code", code)
            add("redirect_uri", clientRegistration.redirectUri)
            add("client_id", clientRegistration.clientId)
            add("client_secret", jwtGenerator.generate())
        }

        val response = restTemplate.postForEntity(
            clientRegistration.providerDetails.tokenUri,
            HttpEntity(body, headers),
            Map::class.java
        )

        val rawBody = response.body
        val responseBody = if (rawBody is Map<*, *>) {
            @Suppress("UNCHECKED_CAST")
            rawBody as Map<String, Any>
        } else {
            throw IllegalArgumentException("Apple 응답 파싱 실패: 응답 형식이 Map이 아님")
        }

        log.debug("Apple OAuth id_token: ${responseBody["id_token"]}")

        val accessToken = responseBody["access_token"]?.toString()
            ?: throw IllegalStateException("Apple 응답에서 access_token 누락")

        val expiresIn = (responseBody["expires_in"] as? Number)?.toLong()
            ?: throw IllegalStateException("Apple 응답에서 expires_in 누락")

        return OAuth2AccessTokenResponse.withToken(accessToken)
            .tokenType(OAuth2AccessToken.TokenType.BEARER)
            .expiresIn(expiresIn)
            .refreshToken(responseBody["refresh_token"]?.toString())
            .additionalParameters(responseBody)
            .build()

    }

    companion object {
        private const val GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code"
    }
}
