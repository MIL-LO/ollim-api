package com.millo.ollim.common.config

import com.millo.ollim.auth.service.CustomOidcUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

/**
 * Spring Security 설정
 */
@Configuration
class SecurityConfig(
    private val customOidcUserService: CustomOidcUserService    // 구글처리
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/login/**", "/oauth2/**").permitAll()
                it.anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .userInfoEndpoint { endpoint ->
                        endpoint
                            .oidcUserService(customOidcUserService) // OIDCUserService도 등록
                    }
                    .defaultSuccessUrl("/", true)
            }

        return http.build()
    }
}
