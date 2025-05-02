// src/main/kotlin/com/millo/ollim/common/config/SecurityConfig.kt
package com.millo.ollim.common.config

import com.millo.ollim.auth.service.CustomOidcUserService
import com.millo.ollim.common.util.JwtAuthenticationFilter
import com.millo.ollim.common.util.JwtTokenProvider
import com.millo.ollim.common.util.OAuth2SuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfigurationSource

/**
 * Spring Security 설정 클래스
 */
@Configuration
class SecurityConfig(
    private val oAuth2SuccessHandler: OAuth2SuccessHandler,
    private val customOidcUserService: CustomOidcUserService,
    private val corsConfigurationSource: CorsConfigurationSource,
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate: StringRedisTemplate
) {

    /**
     * JwtAuthenticationFilter 빈 등록
     */
    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtTokenProvider, redisTemplate)
    }

    /**
     * Security Filter Chain 구성
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/api/v1/auth/**",
                        "/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/actuator/health",
                        "/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .userInfoEndpoint {
                        it.oidcUserService(customOidcUserService)
                    }
                    .successHandler(oAuth2SuccessHandler)
            }
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
