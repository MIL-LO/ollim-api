package com.millo.ollim.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 토큰 관련 DTO
 */
class TokenDTO {

    /**
     * RefreshToken 기반 AccessToken 재발급 요청 DTO
     */
    @Schema(name = "TokenDTO.Request", description = "AccessToken 재발급 요청 DTO")
    data class Request(

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
    @Schema(name = "TokenDTO.Response", description = "JWT 토큰 응답 DTO")
    data class Response(

        @field:Schema(
            description = "Access Token",
            example = "eyJhbGciOiJIUzI1NiJ9"
        )
        val accessToken: String,

        @field:Schema(
            description = "Refresh Token",
            example = "aef0d019-4535-421f-bc0f-1dd18e0706d1",
        )
        val refreshToken: String
    )
}
