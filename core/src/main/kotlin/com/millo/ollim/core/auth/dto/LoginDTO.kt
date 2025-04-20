package com.millo.ollim.core.auth.dto

import com.millo.ollim.core.auth.domain.OAuthProvider
import java.util.UUID

/**
 * 소셜 로그인 Request, Response DTO 클래스
 */
object LoginDTO {

    /**
     * 소셜 로그인 Request DTO
     * @param provider 로그인 제공자 (Google, Apple)
     * @param code OAuth2 인가 코드
     */
    data class Request(
        val provider: OAuthProvider,
        val code: String
    )

    /**
     * 로그인 Response DTO
     * @param accessToken 사용자에게 전달할 JWT Access Token
     * @param refreshToken Redis에 저장되는 Refresh Token
     * @param isNewUser 신규 가입 여부
     * @param userId 사용자 ID
     * @param role 사용자 역할 (USER, ADMIN)
     * @param nickname 닉네임
     */
    data class Response(
        val accessToken: String,
        val refreshToken: String,
        val isNewUser: Boolean,
        val userId: UUID,
        val role: String,
        val nickname: String
    )
}
