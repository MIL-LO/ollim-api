package com.millo.ollim.common.util

import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.user.domain.UserRole
import com.millo.ollim.user.domain.UserStatus
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
    fun generateAccessToken(
        userId: UUID,
        role: UserRole,
        status: UserStatus,
        email: String,
        nickname: String?
    ): String {
        val now = Date()
        val expiry = Date(now.time + accessTokenExpiration)

        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("role", role.name)
            .claim("status", status.name)
            .claim("email", email)
            .claim("nickname", nickname ?: "익명")
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * RefreshToken 발급
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
     * 인증 객체 복원
     */
    fun getAuthentication(token: String): UsernamePasswordAuthenticationToken? {
        return try {
            val claims = parseClaims(token)

            val userId = UUID.fromString(claims.subject)
            val role = UserRole.valueOf(claims["role"]?.toString() ?: error("Missing role claim"))
            val status = UserStatus.valueOf(claims["status"]?.toString() ?: error("Missing status claim"))
            val email = claims["email"]?.toString() ?: "unknown"
            val nickname = claims["nickname"]?.toString()

            val cleanClaims = claims.filterKeys { it != "iat" && it != "exp" }

            val idToken = OidcIdToken.withTokenValue(token)
                .issuedAt(claims.issuedAt?.toInstant())
                .expiresAt(claims.expiration?.toInstant())
                .claims { it.putAll(cleanClaims) }
                .build()

            val userPrincipal = UserPrincipal(
                userId = userId,
                email = email,
                role = role,
                status = status,
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
     * Redis 키 생성 유틸
     */
    fun getRefreshTokenKey(userId: UUID) = "refresh_token:$userId"
    fun getBlacklistKey(accessToken: String) = "blacklist:$accessToken"

    /**
     * 유효성 검사
     */
    fun validateToken(token: String): Boolean = try {
        !parseClaims(token).expiration.before(Date())
    } catch (e: Exception) {
        log.warn("토큰 유효성 검증 실패", e)
        false
    }

    /**
     * 토큰 만료일 추출
     */
    fun getExpiration(token: String): Date = parseClaims(token).expiration

    /**
     * RefreshToken 여부 검사
     */
    fun isRefreshToken(token: String): Boolean = try {
        parseClaims(token)["type"] == "refresh_token"
    } catch (e: Exception) {
        log.warn("RefreshToken 타입 확인 실패", e)
        false
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
     * JWT Claims 파싱
     */
    fun parseClaims(token: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        } catch (e: Exception) {
            log.warn("토큰 파싱 실패", e)
            throw IllegalArgumentException("유효하지 않은 JWT 토큰입니다.")
        }
    }
}
