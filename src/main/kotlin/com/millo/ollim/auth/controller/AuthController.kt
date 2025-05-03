package com.millo.ollim.auth.controller

import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.auth.dto.AuthInfoDTO
import com.millo.ollim.auth.dto.TokenDTO
import com.millo.ollim.auth.service.AuthService
import com.millo.ollim.common.enums.Versions
import com.millo.ollim.common.util.JwtTokenProvider
import com.millo.ollim.user.dto.UserProfileDTO
import com.millo.ollim.user.service.NicknameService
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
 * 인증 및 사용자 관련 API 컨트롤러
 */
@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequestMapping("${Versions.V1}/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val nicknameService: NicknameService
) {

    @GetMapping("/info")
    @Operation(
        summary = "현재 로그인한 사용자 정보 조회",
        description = "AccessToken 기반 인증 후 사용자 정보(ID, 이메일, 상태 등)를 반환합니다."
    )
    fun getCurrentUser(
        @AuthenticationPrincipal user: UserPrincipal?
    ): AuthInfoDTO.AuthInfoResponse {
        if (user == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.")
        }
        return authService.getAuthInfo(user)
    }

    @PostMapping("/signup/profile")
    @Operation(
        summary = "회원가입 추가 정보 작성",
        description = "회원가입 시 작성하는 프로필 정보를 저장하고, 계정 상태를 ACTIVE로 전환합니다."
    )
    fun submitProfile(
        @AuthenticationPrincipal user: UserPrincipal?,
        @RequestBody userProfileRequest: UserProfileDTO.UserProfileRequest
    ): ResponseEntity<Void> {
        if (user == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.")
        }
        authService.registerUserProfile(user.userId, userProfileRequest)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/refresh")
    @Operation(
        summary = "AccessToken 재발급",
        description = "RefreshToken을 검증하고 새로운 AccessToken을 발급합니다."
    )
    fun refreshAccessToken(
        @RequestBody @Valid tokenRequest: TokenDTO.TokenRequest
    ): ResponseEntity<TokenDTO.TokenResponse> {
        val refreshToken = tokenRequest.refreshToken
        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 RefreshToken입니다.")
        }
        val response = authService.refresh(tokenRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/logout")
    @Operation(
        summary = "로그아웃",
        description = "Redis에서 RefreshToken을 삭제하고 AccessToken을 블랙리스트에 등록합니다."
    )
    fun logout(
        @AuthenticationPrincipal user: UserPrincipal?,
        request: HttpServletRequest
    ): ResponseEntity<Void> {
        if (user == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.")
        }
        authService.logoutWithRequest(user, request)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/nickname/suggest")
    @Operation(
        summary = "랜덤 닉네임 추천",
        description = "감정 + 동물 이름 조합으로 랜덤 닉네임을 추천합니다."
    )
    fun suggestNickname(): ResponseEntity<UserProfileDTO.NicknameResponse> {
        val nicknameResponse = nicknameService.generateNickname()
        return ResponseEntity.ok(nicknameResponse)
    }
}
