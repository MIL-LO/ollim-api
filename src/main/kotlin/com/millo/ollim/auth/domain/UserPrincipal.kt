package com.millo.ollim.auth.domain

import com.millo.ollim.user.domain.UserRole
import com.millo.ollim.user.domain.UserStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import java.io.Serializable
import java.util.*

/**
 * OIDC 기반 사용자 인증 정보를 담는 Principal
 */
data class UserPrincipal(
    val userId: UUID,
    @get:JvmName("email") val email: String,
    val role: UserRole,
    val status: UserStatus,
    val nickname: String? = null,
    private val authorities: Collection<GrantedAuthority>,
    private val idToken: OidcIdToken,
    private val userInfo: OidcUserInfo
) : OidcUser, Serializable {

    override fun getName(): String = userId.toString()

    override fun getAttributes(): Map<String, Any?> = buildMap {
        put("userId", userId.toString())
        put("email", email)
        put("role", role)
        put("status", status)
        put("nickname", nickname ?: "익명")
    }

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getClaims(): Map<String, Any> = idToken.claims

    override fun getUserInfo(): OidcUserInfo = userInfo

    override fun getIdToken(): OidcIdToken = idToken
}
