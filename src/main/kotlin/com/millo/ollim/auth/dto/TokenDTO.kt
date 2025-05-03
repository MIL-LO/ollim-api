package com.millo.ollim.auth.dto

import com.millo.ollim.user.domain.UserStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 토큰 관련 DTO
 */
class TokenDTO {

    /**
     * RefreshToken 기반 AccessToken 재발급 요청 DTO
     */
    @Schema(description = "AccessToken 재발급 요청 DTO")
    data class TokenRequest(

        @field:NotBlank
        @field:Schema(
            description = "저장된 Refresh Token",
            required = true,
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
        )
        val refreshToken: String
    )

    /**
     * AccessToken / RefreshToken 응답 DTO
     */
    @Schema(description = "JWT 토큰 응답 DTO")
    data class TokenResponse(

        @field:Schema(
            description = "Access Token",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
        )
        val accessToken: String,

        @field:Schema(
            description = "Refresh Token",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
        )
        val refreshToken: String,

        @field:Schema(
            description = "사용자 상태",
            example = "ACTIVE"
        )
        val status: UserStatus
    )
}
