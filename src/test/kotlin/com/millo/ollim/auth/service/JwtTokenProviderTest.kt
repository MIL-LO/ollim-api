package com.millo.ollim.auth.service

import com.millo.ollim.user.domain.UserRole
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class JwtTokenProviderTest : StringSpec({

    val secret = "ThisIsASecretKeyForJwtThatIsVerySecureAndLongEnough123456"
    val accessTokenExpiration = 60 * 60 * 1000L // 1시간
    val refreshTokenExpiration = 14 * 24 * 60 * 60 * 1000L // 14일

    val jwtTokenProvider = JwtTokenProvider(
        secret = secret,
        accessTokenExpiration = accessTokenExpiration,
        refreshTokenExpiration = refreshTokenExpiration
    )

    "AccessToken 발급 및 claim 파싱 성공" {
        // given
        val userId = UUID.randomUUID()
        val role = UserRole.USER

        // when
        val accessToken = jwtTokenProvider.generateAccessToken(userId, role)
        val claims = jwtTokenProvider.parseClaims(accessToken)

        // then
        accessToken.shouldNotBeEmpty()
        claims.subject shouldBe userId.toString()
        claims["role"] shouldBe role.name
    }

    "RefreshToken 발급 성공" {
        // when
        val refreshToken = jwtTokenProvider.generateRefreshToken()

        // then
        refreshToken.shouldNotBeEmpty()
        val claims = jwtTokenProvider.parseClaims(refreshToken)
        claims.subject shouldBe null
    }
})
