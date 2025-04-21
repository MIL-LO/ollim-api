package com.millo.ollim.auth.dto

import com.millo.ollim.user.domain.ProviderType

/**
 * OAuth 로그인 후 공통으로 사용할 사용자 정보 모델
 * - Google, Apple 등 provider에 관계없이 일관된 형식으로 표현
 */
data class OAuthUserInfo(
    val email: String,
    val name: String?,
    val provider: ProviderType?,
    val oauthId: String
)
