package com.millo.ollim.auth.controller

import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.user.dto.UserProfileDTO
import com.millo.ollim.common.enums.Versions
import com.millo.ollim.user.service.UserProfileService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * 인증된 사용자 정보 확인용 Controller
 */
@RestController
@RequestMapping("${Versions.V1}/auth")
class AuthController(
    private val userProfileService: UserProfileService
) {

    /**
     * 로그인한 회원 정보 추출 (간단정보)
     */
    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal user: UserPrincipal?): Map<String, Any?> {
        if (user == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.")
        }

        return mapOf(
            "userId" to user.userId.toString(),
            "email" to user.email,
            "role" to user.role,
            "nickname" to user.nickname
        )
    }

    /**
     * 유저 회원가입 프로필 작성
     */
    @PostMapping("/signup/profile")
    fun submitProfile(
        @AuthenticationPrincipal user: UserPrincipal?,
        @RequestBody request: UserProfileDTO.Request
    ): ResponseEntity<Void> {
        if (user == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.")
        }

        userProfileService.createOrUpdateProfile(userId = user.userId, request = request)
        return ResponseEntity.ok().build()
    }
}
