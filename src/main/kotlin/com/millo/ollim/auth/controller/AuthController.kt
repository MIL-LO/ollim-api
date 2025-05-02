package com.millo.ollim.auth.controller

import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.auth.dto.TokenDTO
import com.millo.ollim.auth.service.AuthService
import com.millo.ollim.auth.service.RefreshTokenService
import com.millo.ollim.common.enums.Versions
import com.millo.ollim.common.util.TokenExtractor
import com.millo.ollim.user.dto.UserProfileDTO
import com.millo.ollim.user.service.UserProfileService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * 인증 및 사용자 정보 관련 Controller
 */
@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequestMapping("${Versions.V1}/auth")
class AuthController(
    private val userProfileService: UserProfileService,
    private val authService: AuthService,
    private val refreshTokenService: RefreshTokenService
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @GetMapping("/me")
    @Operation(
        summary = "현재 로그인한 사용자 정보 조회",
        description = "AccessToken 기반으로 인증된 사용자의 기본 정보를 반환합니다."
    )
    fun getCurrentUser(
        @AuthenticationPrincipal user: UserPrincipal?
    ): Map<String, Any?> {
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
     * 회원가입 추가 정보 작성
     */
    @PostMapping("/signup/profile")
    @Operation(
        summary = "회원가입 추가 정보 작성",
        description = "회원가입 시 추가 입력하는 프로필 정보를 저장합니다."
    )
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

        /**
     * AccessToken 재발급
     */
    @PostMapping("/refresh")
    @Operation(
        summary = "AccessToken 재발급",
        description = "RefreshToken을 기반으로 AccessToken을 재발급합니다."
    )
    fun refreshAccessToken(
        @RequestBody @Valid request: TokenDTO.Request
    ): ResponseEntity<TokenDTO.Response> {
        // TODO: 로그 지우기
        log.info(">>> [RefreshTokenController] 요청 수신: refreshToken=${request.refreshToken}")
        val response = refreshTokenService.reissueAccessToken(request)
        return ResponseEntity.ok(response)
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    @Operation(
        summary = "로그아웃",
        description = "RefreshToken을 제거하고 AccessToken을 블랙리스트 처리합니다."
    )
    fun logout(
        @AuthenticationPrincipal user: UserPrincipal?,
        request: HttpServletRequest
    ): ResponseEntity<Void> {
        if (user == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.")
        }

        val accessToken = TokenExtractor.extractAccessToken(request)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "AccessToken이 필요합니다.")

        authService.logout(user, accessToken)
        return ResponseEntity.noContent().build()
    }
}
