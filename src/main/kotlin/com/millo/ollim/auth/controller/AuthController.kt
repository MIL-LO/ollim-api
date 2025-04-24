package com.millo.ollim.auth.controller

import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.common.enums.Versions
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 인증된 사용자 정보 확인용 Controller
 */
@RestController
@RequestMapping("${Versions.V1}/auth")
class AuthController {

    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal user: UserPrincipal): Map<String, Any?> {
        return mapOf(
            "userId" to user.userId.toString(),
            "email" to user.email,
            "role" to user.role,
            "nickname" to user.nickname
        )
    }
}
