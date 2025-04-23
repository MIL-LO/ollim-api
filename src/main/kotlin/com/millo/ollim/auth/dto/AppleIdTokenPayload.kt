package com.millo.ollim.auth.dto

/**
 * Apple OAuth2의 id_token을 디코딩하여 파싱한 사용자 정보
 */
data class AppleIdTokenPayload(
    val iss: String,
    val aud: String,
    val exp: Long,
    val iat: Long,
    val sub: String,
    val email: String,
    val email_verified: String
)
