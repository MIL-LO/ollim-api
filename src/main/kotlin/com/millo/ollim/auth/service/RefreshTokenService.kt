package com.millo.ollim.auth.service

import com.millo.ollim.auth.dto.TokenDTO

/**
 * RefreshToken 검증 및 AccessToken 재발급 처리 Interface
 */
interface RefreshTokenService {

    /**
     * 클라이언트로부터 전달받은 refreshToken을 검증하여
     * 새로운 AccessToken과 RefreshToken을 발급한다.
     *
     * @param request RefreshToken 요청 정보
     * @return AccessToken과 새로 발급한 RefreshToken 응답
     */
    fun reissueAccessToken(request: TokenDTO.Request): TokenDTO.Response
}
