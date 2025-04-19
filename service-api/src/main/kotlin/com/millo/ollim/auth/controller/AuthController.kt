package com.millo.ollim.auth.controller

import com.millo.ollim.core.auth.dto.LoginDTO
import com.millo.ollim.core.auth.port.AuthService
import com.millo.ollim.core.common.enums.Versions
import org.springframework.web.bind.annotation.*

/**
 * 인증 관련 API 컨트롤러
 */
@RestController
@RequestMapping("${Versions.V1}/auth")
class AuthController(
    private val authService: AuthService
) {

    /**
     * 소셜 로그인 API
     * @param request OAuth 인가 코드 및 provider 정보
     * @return 로그인 결과 (JWT 토큰 등)
     */
    @PostMapping("/login")
    fun login(@RequestBody request: LoginDTO.Request): LoginDTO.Response {
        return authService.login(request)
    }
}
