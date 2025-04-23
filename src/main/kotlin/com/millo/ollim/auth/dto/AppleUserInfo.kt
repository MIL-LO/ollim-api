package com.millo.ollim.auth.dto

/**
 * Apple OAuth 로그인 시 반환되는 id_token의 정보를 매핑한 사용자 정보 DTO
 */
data class AppleUserInfo(
    val sub: String,       // 고유 사용자 식별자
    val email: String      // Apple 계정 이메일
)
