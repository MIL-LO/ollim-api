package com.millo.ollim.common.util

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource

/**
 * 애플 클라이언트 시크릿 설정파일 주입 클래수
 */
@Configuration
class AppleClientSecretInjector(
    private val jwtGenerator: AppleJwtGenerator,
    private val env: ConfigurableEnvironment
) {

    @PostConstruct
    fun injectSecret() {
        val jwt = jwtGenerator.generate()
        env.propertySources.addFirst(
            MapPropertySource("apple-oauth-secret", mapOf("APPLE_CLIENT_SECRET" to jwt))
        )
    }
}
