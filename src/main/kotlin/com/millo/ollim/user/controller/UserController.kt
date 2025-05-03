package com.millo.ollim.user.controller

import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.auth.service.AuthService
import com.millo.ollim.common.enums.Versions
import com.millo.ollim.user.dto.UserProfileDTO
import com.millo.ollim.user.service.UserProfileService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * 회원 전용 API 컨트롤러 (마이페이지 등)
 */
@Tag(name = "User", description = "회원 전용 API")
@RestController
@RequestMapping("${Versions.V1}/users")
class UserController(
    private val userProfileService: UserProfileService,
    private val authService: AuthService
) {

    /**
     * 내 프로필 정보 조회
     */
    @GetMapping("/profile")
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 프로필 정보를 조회합니다.")
    fun getMyProfile(
        @AuthenticationPrincipal user: UserPrincipal?
    ): ResponseEntity<UserProfileDTO.UserProfileResponse> {
        if (user == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.")
        }

        val profile = userProfileService.getProfile(user.userId)
        return ResponseEntity.ok(profile)
    }

    /**
     * 내 프로필 정보 수정
     */
    @PutMapping("/profile")
    @Operation(summary = "내 정보 수정", description = "현재 로그인한 사용자의 프로필 정보를 수정합니다.")
    fun updateMyProfile(
        @AuthenticationPrincipal user: UserPrincipal?,
        @RequestBody @Valid request: UserProfileDTO.UserProfileRequest
    ): ResponseEntity<Void> {
        if (user == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.")
        }

        userProfileService.createOrUpdateProfile(user.userId, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/withdraw")
    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자의 계정을 탈퇴하고 로그아웃 처리합니다.")
    fun withdrawUser(
        @AuthenticationPrincipal user: UserPrincipal?,
        request: HttpServletRequest
    ): ResponseEntity<Void> {
        if (user == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.")
        }

        // 사용자 상태를 WITHDRAWN으로 변경
        userProfileService.withdraw(user.userId)

        // 로그아웃 처리 (Redis 토큰 제거 및 블랙리스트 등록)
        authService.logoutWithRequest(user, request)

        return ResponseEntity.noContent().build()
    }
}
