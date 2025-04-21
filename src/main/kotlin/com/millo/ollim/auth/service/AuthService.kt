package com.millo.ollim.auth.service

import com.millo.ollim.auth.dto.AuthDTO

/**
 * OAuth 인증 처리 Interface
 */
interface AuthService {

    /**
     * Google OAuth 인증 URL 반환
     */
    fun generateGoogleAuthorizeUrl(): String

    /**
     * Google OAuth 로그인 처리
     */
    fun loginWithGoogle(request: AuthDTO.OAuthLoginRequest): AuthDTO.OAuthLoginResponse
}
