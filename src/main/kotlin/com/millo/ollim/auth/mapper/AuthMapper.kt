package com.millo.ollim.auth.mapper

import com.millo.ollim.auth.dto.OAuthUserInfo
import com.millo.ollim.user.domain.ProviderType
import org.springframework.stereotype.Component

@Component
class AuthMapper {

    // Google 응답을 OAuthUserInfo로 변환
    fun convertGoogleToOAuthUserInfo(response: Map<String, Any>): OAuthUserInfo {
        val email = response["email"] as String
        val name = response["name"] as? String
        val oauthId = response["id"] as String

        return OAuthUserInfo(
            email = email,
            name = name,
            provider = ProviderType.GOOGLE,
            oauthId = oauthId
        )
    }

    // Apple 응답을 OAuthUserInfo로 변환
    fun convertAppleToOAuthUserInfo(response: Map<String, Any>): OAuthUserInfo {
        val email = response["email"] as String
        val name = response["name"] as? String
        val oauthId = response["sub"] as String

        return OAuthUserInfo(
            email = email,
            name = name,
            provider = ProviderType.APPLE,
            oauthId = oauthId
        )
    }
}
