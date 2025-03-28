package com.millo.ollim.app.config

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerOAuthPropertyDecryptor {

    @Value("\${springdoc.swagger-ui.oauth.client-id}")
    private val googleClientId: String? = null

    @Value("\${springdoc.swagger-ui.oauth.client-secret}")
    private val googleClientSecret: String? = null

    @PostConstruct
    fun injectSwaggerOAuthProperties() {
        System.setProperty("springdoc.swagger-ui.oauth.client-id", googleClientId ?: "")
        System.setProperty("springdoc.swagger-ui.oauth.client-secret", googleClientSecret ?: "")
    }
}
