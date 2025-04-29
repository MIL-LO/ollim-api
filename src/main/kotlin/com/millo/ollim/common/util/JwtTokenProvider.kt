package com.millo.ollim.common.util

import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.user.domain.UserRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

/**
 * JWT 토큰 발급 및 파싱 유틸리티
 * - AccessToken: 사용자 인증 정보 포함
 * - RefreshToken: userId + type=refresh_token claim 포함
 */
@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") secret: String,
    @Value("\${jwt.access-token-expiration}") private val accessTokenExpiration: Long,
    @Value("\${jwt.refresh-token-expiration}") private val refreshTokenExpiration: Long
) {

    private val key: Key = Keys.hmacShaKeyFor(secret.toByteArray())
    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * AccessToken 발급
     */
    fun generateAccessToken(userId: UUID, role: UserRole, email: String, nickname: String?): String {
        val now = Date()
        val expiry = Date(now.time + accessTokenExpiration)

        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("role", role.name)
            .claim("email", email)
            .claim("nickname", nickname ?: "익명")
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * RefreshToken 발급 (userId + type=refresh_token)
     */
    fun generateRefreshToken(userId: UUID): String {
        val now = Date()
        val expiry = Date(now.time + refreshTokenExpiration)

        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("type", "refresh_token")
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * Redis에서 RefreshToken 저장 시 사용할 키 생성
     */
    fun getRefreshTokenKey(userId: UUID): String {
        return "refresh_token:$userId"
    }

    /**
     * AccessToken 기반 인증 객체 복원
     */
    fun getAuthentication(token: String): UsernamePasswordAuthenticationToken? {
        return try {
            val claims = parseClaims(token)

            val userId = UUID.fromString(claims.subject)
            val roleStr = claims["role"]?.toString() ?: throw IllegalArgumentException("Missing role claim")
            val role = UserRole.valueOf(roleStr)
            val email = claims["email"]?.toString() ?: "unknown"
            val nickname = claims["nickname"]?.toString() ?: "익명"

            val cleanClaims = claims.filterKeys { it != "iat" && it != "exp" }

            val idToken = OidcIdToken.withTokenValue(token)
                .issuedAt(claims.issuedAt?.toInstant())
                .expiresAt(claims.expiration?.toInstant())
                .claims { it.putAll(cleanClaims) }
                .build()

            val userPrincipal = UserPrincipal(
                userId = userId,
                email = email,
                role = role.name,
                nickname = nickname,
                authorities = listOf(SimpleGrantedAuthority("ROLE_${role.name}")),
                idToken = idToken,
                userInfo = OidcUserInfo(cleanClaims)
            )

            UsernamePasswordAuthenticationToken(userPrincipal, token, userPrincipal.authorities)
        } catch (e: Exception) {
            log.warn("인증 객체 복원 실패: ${e.message}", e)
            null
        }
    }

    /**
     * RefreshToken 여부 확인
     */
    fun isRefreshToken(token: String): Boolean {
        return try {
            val claims = parseClaims(token)
            claims["type"] == "refresh_token"
        } catch (e: Exception) {
            log.warn("RefreshToken 타입 확인 실패", e)
            false
        }
    }

    /**
     * RefreshToken으로부터 userId 추출
     */
    fun getUserIdFromRefreshToken(token: String): UUID {
        val claims = parseClaims(token)

        if (claims["type"] != "refresh_token") {
            throw IllegalArgumentException("RefreshToken이 아닙니다.")
        }

        return UUID.fromString(claims.subject)
    }

    /**
     * 토큰 유효성 검증
     */
    fun validateToken(token: String): Boolean {
        return try {
            val claims = parseClaims(token)
            !claims.expiration.before(Date())
        } catch (e: Exception) {
            log.warn("토큰 유효성 검증 실패", e)
            false
        }
    }

    /**
     * JWT Claims 파싱
     */
    fun parseClaims(token: String): Claims {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            log.warn("토큰 파싱 실패", e)
            throw IllegalArgumentException("유효하지 않은 JWT 토큰입니다.")
        }
    }
}
