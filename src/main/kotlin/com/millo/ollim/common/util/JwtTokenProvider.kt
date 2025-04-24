package com.millo.ollim.common.util

import com.millo.ollim.user.domain.UserRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

/**
 * JWT Access/Refresh 토큰 발급 및 파싱 처리
 */
@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") secret: String,
    @Value("\${jwt.access-token-expiration}") private val accessTokenExpiration: Long,
    @Value("\${jwt.refresh-token-expiration}") private val refreshTokenExpiration: Long,
    ) {

    private  val key: Key = Keys.hmacShaKeyFor(secret.toByteArray())

    /**
     * 엑세스 토큰 생성
     */
    fun generateAccessToken(userId: UUID, role: UserRole): String {
        val now = Date()
        val expiry = Date(now.time + accessTokenExpiration)

        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("role", role.name)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }


    /**
     * 리프레시 토큰 생성
     */
    fun generateRefreshToken(): String {
        val now = Date()
        val expiry = Date(now.time + refreshTokenExpiration)

        return Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * 클래임파싱
     */
    fun parseClaims(token: String) : Claims =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
}
