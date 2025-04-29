package com.millo.ollim.auth.controller

import com.millo.ollim.auth.dto.TokenDTO
import com.millo.ollim.auth.service.RefreshTokenService
import com.millo.ollim.common.enums.Versions
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Refresh Token 기반 AccessToken 재발급 Controller
 */
@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequestMapping("${Versions.V1}/auth")
class RefreshTokenController(
    private val refreshTokenService: RefreshTokenService
) {

    private val log = LoggerFactory.getLogger(this::class.java)

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
}
