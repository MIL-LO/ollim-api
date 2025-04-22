package com.millo.ollim.auth.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity

@RestController
class OAuthTestController {

    /**
     * Google OAuth 성공 후 리다이렉트될 임시 확인용 엔드포인트
     */
    @GetMapping("/auth/oauth-success")
    fun oauthSuccess(): ResponseEntity<String> {
        return ResponseEntity.ok("Google OAuth 로그인 성공")
    }
}
