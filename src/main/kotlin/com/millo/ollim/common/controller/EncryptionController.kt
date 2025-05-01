package com.millo.ollim.common.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.jasypt.encryption.StringEncryptor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Jasypt 문자열 암호화를 위한 Controller
 */
@Tag(name = "Encryption", description = "암호화 관련 API")
@RestController
@RequestMapping("/encrypt")
class EncryptionController(
    @Qualifier("jasyptStringEncryptor") private val stringEncryptor: StringEncryptor
) {


    @PostMapping
    @Operation(
        summary = "Jasypt 암호화",
        description = "값을 넣어서 Jasypt 암호화를 진행합니다."
    )
    fun encrypt(@RequestBody request: EncryptRequest): ResponseEntity<EncryptResponse> {
        val encrypted = stringEncryptor.encrypt(request.plainText)
        return ResponseEntity.ok(EncryptResponse("ENC($encrypted)"))
    }

    data class EncryptRequest(
        val plainText: String
    )

    data class EncryptResponse(
        val encrypted: String
    )
}
