package com.millo.ollim.auth.controller

import com.millo.ollim.auth.dto.AppleUserInfo
import com.millo.ollim.auth.mapper.toUserInfo
import com.millo.ollim.auth.util.AppleIdTokenParser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class OAuthTestController(
    private val appleIdTokenParser: AppleIdTokenParser
) {
    /**
     * Apple id_token 디코딩 후 AppleUserInfo 반환
     */
    @PostMapping("/decode-id-token")
    fun decodeIdToken(@RequestBody token: String): ResponseEntity<AppleUserInfo> {
        val payload = appleIdTokenParser.parse(token)
        return ResponseEntity.ok(payload.toUserInfo())
    }

    /**
     * OAuth 성공 페이지 (임시)
     */
    @GetMapping("/oauth-success")
    fun oauthSuccess(): ResponseEntity<String> {
        return ResponseEntity.ok("OAuth 로그인 성공")
    }
}
