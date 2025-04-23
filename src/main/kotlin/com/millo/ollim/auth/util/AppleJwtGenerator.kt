package com.millo.ollim.auth.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.interfaces.ECPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant
import java.util.*

/**
 * Apple OAuth2 로그인 시 Apple에 전송할 client_secret JWT를 동적으로 생성하는 클래스입니다.
 */
@Component
class AppleJwtGenerator {

    @Value("\${APPLE_TEAM_ID}")
    lateinit var teamId: String

    @Value("\${APPLE_KEY_ID}")
    lateinit var keyId: String

    @Value("\${APPLE_CLIENT_ID}")
    lateinit var clientId: String

    @Value("\${APPLE_PRIVATE_KEY_PATH}")
    lateinit var privateKeyPath: String

    companion object {
        private val logger = LoggerFactory.getLogger(AppleJwtGenerator::class.java)
    }

    @PostConstruct
    fun init() {
        logger.info("AppleJwtGenerator initialized with:")
        logger.info("Team ID: {}", teamId)
        logger.info("Key ID: {}", keyId)
        logger.info("Client ID: {}", clientId)
        logger.info("Private Key Path: {}", privateKeyPath)
    }

    fun generate(): String {
        return try {
            val now = Instant.now()
            val exp = now.plusSeconds(300)
            val privateKey = getPrivateKey()

            Jwts.builder()
                .setHeaderParam("kid", keyId)
                .setIssuer(teamId)
                .setSubject(clientId)
                .setAudience("https://appleid.apple.com")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact()
        } catch (e: Exception) {
            logger.error("Failed to generate JWT: {}", e.message, e)
            throw RuntimeException("JWT generation failed", e)
        }
    }

    // TODO: Key 암화화
    private fun getPrivateKey(): ECPrivateKey {
        return try {
            // 현재 working directory 기준 상대 경로 -> 절대 경로 변환
            val fullPath = Paths.get(System.getProperty("user.dir"), privateKeyPath).normalize().toAbsolutePath()
            logger.debug("Reading Apple private key from path: {}", fullPath)

            val pem = String(Files.readAllBytes(fullPath))

            val cleanKey = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\\s".toRegex(), "")
            logger.debug("Cleaned PEM: {}", cleanKey)

            val keyBytes = Base64.getDecoder().decode(cleanKey)
            val keySpec = PKCS8EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance("EC")
            keyFactory.generatePrivate(keySpec) as ECPrivateKey
        } catch (e: Exception) {
            logger.error("Private key load failed: {}", e.message, e)
            throw RuntimeException("Private key load failed", e)
        }
    }
}
