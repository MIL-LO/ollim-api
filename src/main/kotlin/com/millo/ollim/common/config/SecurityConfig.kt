package com.millo.ollim.common.config

import com.millo.ollim.auth.service.AppleOAuthClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.web.SecurityFilterChain

/**
 * Spring Security의 전반적인 보안 설정 클래스
 * - OAuth2 로그인, 인증 필터, 공통 예외처리 등을 포함
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val appleOAuthClient: OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>
) {

        @Bean
    fun appleAccessTokenResponseClient(
        appleOAuthClient: AppleOAuthClient
    ): OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
        return appleOAuthClient
    }

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
                        "/actuator/**",
                        "/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .defaultSuccessUrl("/auth/oauth-success", true)
                    .tokenEndpoint { tokenEndpoint ->
                        tokenEndpoint
                            .accessTokenResponseClient(appleOAuthClient)
                    }
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
