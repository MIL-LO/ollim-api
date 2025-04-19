package com.millo.ollim.core.auth.port

import com.millo.ollim.core.auth.dto.LoginDTO

/**
 * 인증 처리 서비스 (OAuth 인증 흐름 전체를 담당)
 */
interface AuthService {

    /**
     * OAuth2 로그인 요청 처리
     * @param request LoginDTO.Request (provider, code)
     * @return LoginDTO.Response (accessToken, refreshToken, 유저정보 등)
     */
    fun login(request: LoginDTO.Request): LoginDTO.Response
}
