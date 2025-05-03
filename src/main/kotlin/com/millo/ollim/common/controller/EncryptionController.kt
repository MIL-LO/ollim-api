package com.millo.ollim.common.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.jasypt.encryption.StringEncryptor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

/**
 * Jasypt 문자열 암호화 및 Base64 유틸리티 Controller
 */
@Tag(name = "Encryption", description = "암호화 및 Base64 관련 API")
@RestController
@RequestMapping("/encrypt")
class EncryptionController(
    @Qualifier("jasyptStringEncryptor") private val stringEncryptor: StringEncryptor
) {

    @PostMapping("/jasypt")
    @Operation(summary = "Jasypt 암호화", description = "값을 넣어서 Jasypt 암호화를 진행합니다.")
    fun encrypt(@RequestBody request: JasyptRequest): ResponseEntity<JasyptResponse> {
        val encrypted = stringEncryptor.encrypt(request.jasyptRequest)
        return ResponseEntity.ok(JasyptResponse("ENC($encrypted)"))
    }

    @PostMapping("/base64/encode")
    @Operation(summary = "Base64 인코딩 (JSON)", description = "입력한 문자열을 Base64로 인코딩합니다.")
    fun encodeBase64(@RequestBody request: Base64Request): ResponseEntity<Base64Response> {
        val normalized = request.base64Request.replace("\\n", "\n")
        val encoded = Base64.getEncoder().encodeToString(normalized.toByteArray(Charsets.UTF_8))
        return ResponseEntity.ok(Base64Response(encoded))
    }

    @PostMapping("/base64/encode/file", consumes = ["multipart/form-data"])
    @Operation(summary = "Base64 인코딩 (파일)", description = "업로드한 파일을 Base64로 인코딩합니다.")
    fun encodeBase64FromFile(@RequestPart("file") file: MultipartFile): ResponseEntity<Base64Response> {
        val content = file.bytes
        val encoded = Base64.getEncoder().encodeToString(content)
        return ResponseEntity.ok(Base64Response(encoded))
    }

    @PostMapping("/base64/decode")
    @Operation(summary = "Base64 디코딩", description = "Base64 인코딩된 문자열을 디코딩합니다.")
    fun decodeBase64(@RequestBody request: Base64Request): ResponseEntity<Base64Response> {
        val decoded = String(Base64.getDecoder().decode(request.base64Request), Charsets.UTF_8)
        return ResponseEntity.ok(Base64Response(decoded))
    }

    data class JasyptRequest(val jasyptRequest: String)
    data class JasyptResponse(val jasyptResponse: String)
    data class Base64Request(val base64Request: String)
    data class Base64Response(val base64Response: String)
}
