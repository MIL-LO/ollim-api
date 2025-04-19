package com.millo.ollim.infrastructure.auth.adapter

import com.millo.ollim.core.auth.domain.OAuthProvider
import com.millo.ollim.core.auth.domain.OAuthUserInfo
import com.millo.ollim.core.auth.port.OAuthService
import org.springframework.stereotype.Component

/**
 * Google OAuth 연동 구현체
 * - OAuthService를 구현하며, Google 인증에 대한 실제 처리
 */
@Component
class GoogleOAuthServiceImpl : OAuthService {

    /**
     * 인가 코드를 통해 Google Access Token을 받아오는 함수
     * @param provider OAuth 제공자 (GOOGLE)
     * @param code 인가 코드
     * @return Access Token (현재는 Stub)
     */
    override fun getAccessToken(provider: OAuthProvider, code: String): String {
        if (provider != OAuthProvider.GOOGLE) {
            throw IllegalArgumentException("지원하지 않는 OAuth 제공자입니다.")
        }
        return "access_token_from_google" // TODO: 실제 구현 필요
    }

    /**
     * Access Token을 통해 사용자 정보를 받아오는 함수
     * @param provider OAuth 제공자
     * @param code 인가 코드
     * @return OAuthUserInfo (현재는 Stub)
     */
    override fun getUserInfo(provider: OAuthProvider, code: String): OAuthUserInfo {
        return OAuthUserInfo(
            email = "example@gmail.com",
            nickname = "google_user",
            provider = provider.name,
            providerId = "google_123456"
        )
    }

    /**
     * 현재 구현체가 Google OAuthProvider를 지원하는지 여부
     */
    override fun supports(provider: OAuthProvider): Boolean {
        return provider == OAuthProvider.GOOGLE
    }
}
