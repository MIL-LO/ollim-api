package com.millo.ollim.auth.service

import OAuthUserInfo
import com.millo.ollim.auth.dto.AppleUserInfo
import com.millo.ollim.auth.dto.GoogleUserInfo
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service

/**
 * Apple / Google OIDC 로그인 처리
 */
@Service
class CustomOidcUserService(
    private val authService: AuthService
) : OidcUserService() {

    override fun loadUser(userRequest: OidcUserRequest): OidcUser {
        val oidcUser = super.loadUser(userRequest)

        // 소셜 로그인 provider 구분 (google, apple)
        val registrationId = userRequest.clientRegistration.registrationId.lowercase()

        val userInfo: OAuthUserInfo = when (registrationId) {
            "google" -> GoogleUserInfo(
                providerId = oidcUser.attributes["sub"] as String,
                email = oidcUser.attributes["email"] as String,
                name = oidcUser.attributes["name"] as? String
            )
            "apple" -> AppleUserInfo(
                sub = oidcUser.attributes["sub"] as String,
                email = oidcUser.attributes["email"] as String,
                name = oidcUser.attributes["name"] as? String
            )
            else -> throw IllegalArgumentException("지원하지 않는 소셜 로그인입니다. [$registrationId]")
        }

        authService.saveOrUpdateUser(userInfo)
        return oidcUser
    }
}
