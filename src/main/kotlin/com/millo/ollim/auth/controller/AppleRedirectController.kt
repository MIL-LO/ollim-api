package com.millo.ollim.auth.controller

import com.millo.ollim.auth.dto.AuthDTO
import com.millo.ollim.auth.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Apple OAuth 리디렉션을 처리하는 전용 컨트롤러
 * - Apple 서버는 이 경로로 POST 요청을 보냄
 * - redirect_uri: https://api.millo-ollim.com/login/oauth2/code/apple
 */
@RestController
class AppleRedirectController(
    private val authService: AuthService
) {

    /**
     * Apple OAuth 리디렉션 처리
     * - Apple이 인증 성공 후 POST로 code, state 등을 전달
     * - 내부적으로 AuthService를 호출하여 로그인 처리
     */
    @PostMapping("/login/oauth2/code/apple")
    fun handleAppleRedirect(
        @RequestParam code: String,
        @RequestParam(required = false) id_token: String?,
        @RequestParam(required = false) state: String?
    ): ResponseEntity<AuthDTO.OAuthLoginResponse> {
        val response = authService.loginWithApple(
            AuthDTO.OAuthLoginRequest(code)
        )
        return ResponseEntity.ok(response)
    }
}
