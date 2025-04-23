package com.millo.ollim.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

/**
 * RestTemplate을 전역적으로 사용하기 위한 설정 클래스
 */
@Configuration
class RestTemplateConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
