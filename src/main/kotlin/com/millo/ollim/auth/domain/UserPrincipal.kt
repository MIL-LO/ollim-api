package com.millo.ollim.auth.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import java.io.Serializable
import java.util.*

/**
 * OIDC 기반 사용자 인증 정보를 담는 Principal
 */
data class UserPrincipal(
    val userId: UUID,
    @get:JvmName("emailValue") val email: String,
    val role: String,
    val nickname: String? = null,
    private val authorityList: Collection<GrantedAuthority>,
    private val idToken: OidcIdToken,
    private val userInfo: OidcUserInfo
) : OidcUser, Serializable {

    override fun getName(): String = userId.toString()

    override fun getAttributes(): Map<String, Any?> = mapOf(
        "userId" to userId.toString(),
        "email" to email,
        "role" to role,
        "nickname" to (nickname ?: "익명")
    )

    override fun getAuthorities(): Collection<GrantedAuthority> = authorityList
    override fun getClaims(): Map<String, Any> =
        getAttributes()
            .filterValues { it != null }
            .mapValues { it.value as Any }
    override fun getUserInfo(): OidcUserInfo = userInfo

    override fun getIdToken(): OidcIdToken = idToken
}
