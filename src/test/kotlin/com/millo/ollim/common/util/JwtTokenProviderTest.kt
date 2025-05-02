package com.millo.ollim.common.util

import com.millo.ollim.user.domain.UserRole
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import java.util.*

/**
 * JwtTokenProvider 단위 테스트
 */
class JwtTokenProviderTest : StringSpec({

    val secret = "01234567890123456789012345678901"
    val accessTokenExpiry = 1000 * 60 * 15L // 15분
    val refreshTokenExpiry = 1000 * 60 * 60 * 24 * 14L // 14일

    val jwtTokenProvider = JwtTokenProvider(
        secret = secret,
        accessTokenExpiration = accessTokenExpiry,
        refreshTokenExpiration = refreshTokenExpiry
    )

    val userId = UUID.randomUUID()
    val role = UserRole.USER
    val email = "test@example.com"
    val nickname = "테스트"

    "AccessToken을 정상적으로 생성한다" {
        val token = jwtTokenProvider.generateAccessToken(userId, role, email, nickname)
        token.shouldNotBeEmpty()
    }

    "RefreshToken을 정상적으로 생성한다" {
        val token = jwtTokenProvider.generateRefreshToken(userId)
        token.shouldNotBeEmpty()
    }

    "AccessToken을 파싱하여 Claims를 추출할 수 있다" {
        val token = jwtTokenProvider.generateAccessToken(userId, role, email, nickname)

        val claims = shouldNotThrowAny {
            jwtTokenProvider.parseClaims(token)
        }

        claims.subject shouldBe userId.toString()
        claims["role"] shouldBe role.name
        claims["email"] shouldBe email
        claims["nickname"] shouldBe nickname
    }

    "AccessToken 생성 시 nickname이 null이면 '익명'으로 처리된다" {
        val token = jwtTokenProvider.generateAccessToken(userId, role, email, null)

        val claims = shouldNotThrowAny {
            jwtTokenProvider.parseClaims(token)
        }

        claims["nickname"] shouldBe "익명"
    }
})
