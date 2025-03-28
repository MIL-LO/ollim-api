package com.millo.ollim.app

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Health", description = "헬스 체크 API")
class HealthCheckController {

    @GetMapping("/health")
    @Operation(summary = "헬스체크", description = "API 서버 상태를 확인합니다.")
    fun healthCheck(): String {
        return "OK"
    }
}
