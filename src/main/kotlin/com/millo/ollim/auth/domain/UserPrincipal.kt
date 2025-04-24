package com.millo.ollim.auth.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import java.io.Serializable

/**
 * 인증된 사용자 정보를 담는 Principal
 */
data class UserPrincipal(
    val userId: String,
    val email: String,
    val role: String,
    val nickname: String? = null,
    private val authorityList: Collection<GrantedAuthority>
) : OAuth2User, Serializable {

    override fun getName(): String = userId

    override fun getAttributes(): Map<String, Any?> = mapOf(
        "userId" to userId,
        "email" to email,
        "role" to role,
        "nickname" to (nickname ?: "익명")
    )

    override fun getAuthorities(): Collection<GrantedAuthority> = authorityList

}
