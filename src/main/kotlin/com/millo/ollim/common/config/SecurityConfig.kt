package com.millo.ollim.common.config

import com.millo.ollim.auth.service.CustomOidcUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * Spring Security 설정
 */
@Configuration
class SecurityConfig(
    private val customOidcUserService: CustomOidcUserService
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .authorizeHttpRequests {
                it.requestMatchers("/**").permitAll()
                it.anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .userInfoEndpoint { endpoint ->
                        endpoint
                            .oidcUserService(customOidcUserService) // OIDCUserService 등록
                    }
                    .defaultSuccessUrl("/", true)
            }

        return http.build()
    }

    /**
     * CORS 처리
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedOrigins = listOf("*")
        config.allowedHeaders = listOf("*")
        config.allowedMethods = listOf("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        return source
    }
}
