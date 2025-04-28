package com.millo.ollim.auth.dto

import OAuthUserInfo

/**
 * Apple OAuth2 사용자 정보 DTO
 */
data class AppleUserInfo(
    private val sub: String,
    private val email: String,
    private val name: String? = null
) : OAuthUserInfo {
    override fun getProvider(): String = "apple"
    override fun getProviderId(): String = sub
    override fun getEmail(): String = email
    override fun getName(): String = name ?: "AppleUser"
}
