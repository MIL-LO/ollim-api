package com.millo.ollim.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Ollim API")
                    .description("감정 다이어리 서비스 올림의 API 문서입니다.")
                    .version("v1.0.0")
            )
    }

    @Bean
    fun v1Api(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("v1")
            .pathsToMatch("/api/v1/**")
            .build()
    }

    @Bean
    fun halEx(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("test")
            .pathsToMatch("/test/**")
            .build()
    }
}
