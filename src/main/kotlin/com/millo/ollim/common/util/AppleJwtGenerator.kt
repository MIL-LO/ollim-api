package com.millo.ollim.common.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant
import java.util.*

/**
 * 애플 JWT 생성 클래스
 */
@Component
class AppleJwtGenerator(
    @Value("\${oauth.apple.team-id}") private val teamId: String,
    @Value("\${oauth.apple.client-id}") private val clientId: String,
    @Value("\${oauth.apple.key-id}") private val keyId: String,
    @Value("\${oauth.apple.secret-key}") private val secretKey: String
) {
    fun generate(): String {
        val now = Instant.now()
        val expiration = now.plusSeconds(60 * 60) // 1시간 유효
        val privateKey = decodePrivateKey()

        return Jwts.builder()
            .setHeaderParam("kid", keyId)
            .setIssuer(teamId)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiration))
            .setAudience("https://appleid.apple.com")
            .setSubject(clientId)
            .signWith(privateKey, SignatureAlgorithm.ES256)
            .compact()
    }

    /**
     * Jasypt로 복호화된 base64 문자열을 이용해 PrivateKey 객체를 생성
     */
    private fun decodePrivateKey(): PrivateKey {
        val decoded = Base64.getDecoder().decode(secretKey)
        val keySpec = PKCS8EncodedKeySpec(decoded)
        return KeyFactory.getInstance("EC").generatePrivate(keySpec)
    }
}
