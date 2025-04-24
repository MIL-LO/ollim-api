package com.millo.ollim.auth.dto

import OAuthUserInfo

/**
 * Google OAuth2 사용자 정보 DTO
 */
data class GoogleUserInfo(
    private val providerId: String,
    private val email: String,
    private val name: String?
) : OAuthUserInfo {
    override fun getProvider(): String = "google"
    override fun getProviderId(): String = providerId
    override fun getEmail(): String = email
    override fun getName(): String = name ?: "GoogleUser"
}
