package com.millo.ollim.auth.dto

import com.millo.ollim.user.domain.UserRole
import com.millo.ollim.user.domain.UserStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

/**
 * 인증된 사용자 정보 응답 DTO
 */
class AuthInfoDTO {

    @Schema(description = "로그인한 사용자 정보 응답 DTO")
    data class AuthInfoResponse(

        @Schema(description = "사용자 고유 식별자", example = "550e8400-e29b-41d4-a716-446655440000")
        val userId: UUID,

        @Schema(description = "사용자 이메일", example = "user@example.com")
        val email: String,

        @Schema(description = "사용자 권한 (USER, ADMIN)", example = "USER")
        val role: UserRole,

        @Schema(description = "계정 상태 (ACTIVE, PENDING, WITHDRAWN)", example = "ACTIVE")
        val status: UserStatus,

        @Schema(description = "닉네임", example = "당황한토끼", nullable = true)
        val nickname: String?
    )
}
