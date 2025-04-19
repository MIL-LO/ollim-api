package com.millo.ollim.core.auth.port

import com.millo.ollim.core.auth.domain.OAuthProvider
import com.millo.ollim.core.auth.domain.OAuthUserInfo

/**
 * 소셜 인증 요청을 외부에 위임하기 위한 포트 Interface
 */
interface OAuthService {
    fun getAccessToken(provider: OAuthProvider, code: String): String
    fun getUserInfo(provider: OAuthProvider, code: String): OAuthUserInfo
}
