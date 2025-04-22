package com.millo.ollim.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

/**
 * Spring Security의 전반적인 보안 설정 클래스입니다.
 * OAuth2 로그인, 인증 필터, 공통 예외처리 등을 포함합니다.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/oauth2/**",
                        "/auth/**",
                        "/login/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/actuator/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    // loginPage는 없어도 기본으로 /login 제공됨
                    .defaultSuccessUrl("/auth/oauth-success", true)
            }
            .logout { logout ->
                logout
                    .logoutUrl("/auth/logout")
                    .logoutSuccessUrl("/auth/login")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
            }

        return http.build()
    }
}
