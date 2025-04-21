package com.millo.ollim.auth.dto

/**
 * OAuth 로그인 요청/응답에 사용되는 DTO 클래스
 * - 내부에 중첩 클래스로 구성
 */
class AuthDTO {

    /**
     * Google OAuth 로그인 요청 DTO
     * - 프론트엔드에서 전달하는 인가 코드 포함
     */
    data class OAuthLoginRequest(
        val code: String
    )

    /**
     * OAuth 로그인 응답 DTO
     * - 백엔드에서 발급한 JWT Access/Refresh Token 포함
     */
    data class OAuthLoginResponse(
        val accessToken: String,
        val refreshToken: String
    )
}
