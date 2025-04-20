package com.millo.ollim.infrastructure.auth.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

/**
 * JWT 토큰 생성 및 암호화 처리 클래스
 */
@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.access-token-expiration}") private val accessTokenValidity: Long,
    @Value("\${jwt.refresh-token-expiration}") private val refreshTokenValidity: Long
) {
    private val key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey))

    /**
     * AccessToken 생성
     * @param userId 사용자 UUID
     * @param role 사용자 역할
     */
    fun createAccessToken(userId: UUID, role: String): String {
        return generateToken(userId.toString(), role, accessTokenValidity)
    }

    /**
     * RefreshToken 생성
     * @param userId 사용자 UUID
     */
    fun createRefreshToken(userId: UUID, role: String): String {
        return generateToken(userId.toString(), role, refreshTokenValidity)
    }

    /**
     * 실제 JWT 생성 로직
     */
    private fun generateToken(subject: String, role: String, validity: Long): String {
        val now = Date()
        val expiration = Date(now.time + validity * 1000)

        return Jwts.builder()
            .setSubject(subject)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
}
