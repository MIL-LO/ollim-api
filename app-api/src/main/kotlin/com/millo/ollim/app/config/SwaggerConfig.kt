package com.millo.ollim.app.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    companion object {
        private const val GOOGLE_SECURITY_SCHEME = "google-oauth"
        private const val APPLE_SECURITY_SCHEME = "apple-oauth"
    }

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Ollim API")
                    .description("감정 다이어리 서비스 올림의 API 문서입니다.")
                    .version("v1.0.0")
            )
            .components(
                Components()
                    .addSecuritySchemes(GOOGLE_SECURITY_SCHEME, googleOAuthScheme())
//                    .addSecuritySchemes(APPLE_SECURITY_SCHEME, appleOAuthScheme())
            )
            .addSecurityItem(SecurityRequirement().addList(GOOGLE_SECURITY_SCHEME))
//            .addSecurityItem(SecurityRequirement().addList(APPLE_SECURITY_SCHEME))
    }

    // Google 인증
    private fun googleOAuthScheme(): SecurityScheme {
        val scopes = Scopes()
            .addString("openid", "OpenID")
            .addString("profile", "사용자 프로필 조회")
            .addString("email", "이메일 조회")

        return SecurityScheme()
            .name(GOOGLE_SECURITY_SCHEME)
            .type(SecurityScheme.Type.OAUTH2)
            .scheme("bearer")
            .bearerFormat("JWT")
            .flows(
                OAuthFlows().authorizationCode(
                    OAuthFlow()
                        .authorizationUrl("https://accounts.google.com/o/oauth2/v2/auth")
                        .tokenUrl("https://oauth2.googleapis.com/token")
                        .scopes(scopes)
                )
            )
    }

    /*
        TODO: APPLE Developer Oauth 등록
        // Apple 인증
        private fun appleOAuthScheme(): SecurityScheme {
            val scopes = Scopes()
                .addString("name", "사용자 이름 조회")
                .addString("email", "이메일 조회")

            return SecurityScheme()
                .name(APPLE_SECURITY_SCHEME)
                .type(SecurityScheme.Type.OAUTH2)
                .scheme("bearer")
                .bearerFormat("JWT")
                .flows(
                    OAuthFlows().authorizationCode(
                        OAuthFlow()
                            .authorizationUrl("https://appleid.apple.com/auth/authorize")
                            .tokenUrl("https://appleid.apple.com/auth/token")
                            .scopes(scopes)
                    )
                )
        }
    */
}
