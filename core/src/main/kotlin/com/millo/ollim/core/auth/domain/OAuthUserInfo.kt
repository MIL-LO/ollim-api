package com.millo.ollim.core.auth.domain

/**
 * OAuth 인증 후 응답받는 UserInfo Model
 */
data class OAuthUserInfo(
    val email: String,
    val nickname: String,
    val provider: String,
    val providerId: String
)
