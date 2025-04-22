package com.millo.ollim.auth.service

import com.millo.ollim.auth.mapper.AuthMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class OAuthUserInfoTest {

    @Autowired
    lateinit var authMapper: AuthMapper

    @Test
    fun testGoogleOAuthConversion() {
        val googleResponse = mapOf(
            "email" to "test@example.com",
            "name" to "Test User",
            "id" to "google-id-12345"
        )

        val userInfo = authMapper.convertGoogleToOAuthUserInfo(googleResponse)

        assertEquals("test@example.com", userInfo.email)
        assertEquals("Test User", userInfo.name)
        assertEquals("google-id-12345", userInfo.oauthId)
        assertEquals("GOOGLE", userInfo.provider.name)
    }

    @Test
    fun testAppleOAuthConversion() {
        val appleResponse = mapOf(
            "email" to "test@example.com",
            "name" to "Test User",
            "sub" to "apple-id-12345"
        )

        val userInfo = authMapper.convertAppleToOAuthUserInfo(appleResponse)

        assertEquals("test@example.com", userInfo.email)
        assertEquals("Test User", userInfo.name)
        assertEquals("apple-id-12345", userInfo.oauthId)
        assertEquals("APPLE", userInfo.provider.name)
    }
}
