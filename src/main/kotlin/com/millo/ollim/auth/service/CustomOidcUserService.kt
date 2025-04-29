package com.millo.ollim.auth.service

import OAuthUserInfo
import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.auth.dto.AppleUserInfo
import com.millo.ollim.auth.dto.GoogleUserInfo
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
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
        val registrationId = userRequest.clientRegistration.registrationId.lowercase()

        // Provider별 사용자 정보 매핑
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

        // DB 저장 또는 업데이트
        val user = authService.saveOrUpdateUser(userInfo)

        // 인증 객체 생성
        return UserPrincipal(
            userId = user.id,
            email = user.email,
            role = user.role.name,
            nickname = user.profile?.nickname,
            authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}")),
            idToken = oidcUser.idToken ?: throw IllegalStateException("OIDC ID Token이 존재하지 않습니다."),
            userInfo = oidcUser.userInfo ?: OidcUserInfo(oidcUser.attributes)
        )
    }
}
