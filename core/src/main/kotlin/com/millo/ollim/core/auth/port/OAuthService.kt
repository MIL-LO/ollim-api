package com.millo.ollim.core.auth.port

import com.millo.ollim.core.auth.domain.OAuthProvider
import com.millo.ollim.core.auth.domain.OAuthUserInfo

/**
 * OAuth 인증 로직을 제공하기 위한 Interface (Port)
 * 구현체는 GoogleOAuthServiceImpl, AppleOAuthServiceImpl로 분리
 */
interface OAuthService {

    /**
     * provider와 인가 코드를 통해 AccessToken을 반환
     */
    fun getAccessToken(provider: OAuthProvider, code: String): String

    /**
     * provider와 인가 코드를 통해 사용자 정보를 반환
     */
    fun getUserInfo(provider: OAuthProvider, code: String): OAuthUserInfo
}
