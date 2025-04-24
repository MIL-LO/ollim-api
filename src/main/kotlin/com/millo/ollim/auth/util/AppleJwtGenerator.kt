package com.millo.ollim.auth.util

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
    @Value("\${oauth.apple.secret-key-path}") private val secretKeyPath: String
) {
    fun generate(): String {
        val now = Instant.now()
        val expiration = now.plusSeconds(60 * 60) // 1시간 유효
        val privateKey = loadPrivateKey()

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

    private fun loadPrivateKey(): PrivateKey {
        val keyContent = Files.readAllLines(Paths.get(secretKeyPath))
            .filterNot { it.startsWith("-----") || it.isBlank() }
            .joinToString("")
        val decoded = Base64.getDecoder().decode(keyContent)
        val keySpec = PKCS8EncodedKeySpec(decoded)
        val kf = KeyFactory.getInstance("EC")
        return kf.generatePrivate(keySpec)
    }
}
