package com.millo.ollim.auth.controller

import com.millo.ollim.auth.dto.AuthDTO
import com.millo.ollim.auth.service.AuthService
import com.millo.ollim.common.enums.Versions
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * OAuth 인증 관련 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("${Versions.V1}/auth")
class AuthController(
    private val authService: AuthService
) {

    /**
     * Google OAuth 인증 URL 생성
     */
    @GetMapping("/google/authorize")
    @Operation(
        summary = "Google OAuth 인증 URL 생성",
        description = "Google OAuth 인증을 위한 URL을 생성하여 반환합니다."
    )
    fun generateGoogleAuthorizeUrl(): ResponseEntity<Map<String, String>> {
        val url = authService.generateGoogleAuthorizeUrl()
        return ResponseEntity.ok(mapOf("authorizeUrl" to url))
    }

    /**
     * Google OAuth 로그인 처리
     * AccessToken, RefreshToken 반환
     */
    @PostMapping("/google/login")
    @Operation(
        summary = "Google OAuth 로그인",
        description = "Google OAuth 인증을 통해 로그인 후 access token과 refresh token을 반환합니다."
    )
    fun loginWithGoogle(
        @RequestBody request: AuthDTO.OAuthLoginRequest
    ): ResponseEntity<AuthDTO.OAuthLoginResponse> {
        val response = authService.loginWithGoogle(request)
        return ResponseEntity.ok(response)
    }

    /**
     * Apple OAuth 로그인 처리
     * AccessToken, RefreshToken 반환
     */
    @PostMapping("/apple/login")
    @Operation(
        summary = "Apple OAuth 로그인",
        description = "Apple OAuth 인증을 통해 로그인 후 access token과 refresh token을 반환합니다."
    )
    fun loginWithApple(
        @RequestBody request: AuthDTO.OAuthLoginRequest
    ): ResponseEntity<AuthDTO.OAuthLoginResponse> {
        val response = authService.loginWithApple(request)
        return ResponseEntity.ok(response)
    }
}
