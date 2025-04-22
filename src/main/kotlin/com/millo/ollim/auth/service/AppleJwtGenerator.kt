package com.millo.ollim.auth.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant
import java.util.*

/**
 * Apple OAuth client_secret JWT 생성기
 */
@Component
class AppleJwtGenerator {

    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${oauth.apple.private-key}")
    private lateinit var encodedKey: String

    /**
     * Apple client_secret JWT 생성
     */
    fun createClientSecret(
        clientId: String,
        teamId: String,
        keyId: String
    ): String {
        val now = Instant.now()
        val expiration = now.plusSeconds(60 * 60 * 6) // 6시간 유효

        val privateKey = loadPrivateKeyFromBase64()

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
     * ID Token 디코딩
     */
    fun decodeIdToken(idToken: String): Map<String, Any> {
        val jwt = Jwts.parserBuilder()
            .build()
            .parseClaimsJwt(idToken.split(".").take(2).joinToString(".") + ".")
        return jwt.body
    }

    /**
     * Jasypt 복호화된 .p8 Base64 문자열에서 PrivateKey 생성
     */
    private fun loadPrivateKeyFromBase64(): PrivateKey {
        log.info("Decoding Apple private key from base64 string")

        val decoded = String(Base64.getDecoder().decode(encodedKey))

        val pem = decoded
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\\s+".toRegex(), "")

        val keySpec = PKCS8EncodedKeySpec(Decoders.BASE64.decode(pem))
        return KeyFactory.getInstance("EC").generatePrivate(keySpec)
    }
}
