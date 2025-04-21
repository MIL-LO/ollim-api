package com.millo.ollim.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

/**
 * WebClient Bean 등록을 위한 공통 설정 클래스
 */
@Configuration
class WebClientConfig {

    /**
     * 기본 WebClient Bean
     */
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder().build()
    }
}
