package com.millo.ollim.auth.mapper

import com.millo.ollim.auth.dto.AppleIdTokenPayload
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class AppleIdTokenMapperKtTest {

    @Test
    fun `AppleIdTokenPayload 를 AppleUserInfo 로 매핑`() {
        // given
        val payload = AppleIdTokenPayload(
            iss = "https://appleid.apple.com",
            aud = "com.millo-ollim.login",
            exp = 1710000000L,
            iat = 1709990000L,
            sub = "abc.abc.abc",
            email = "test@example.com",
            email_verified = "true"
        )

        // when
        val userInfo = payload.toUserInfo()

        // then
        assertEquals(payload.sub, userInfo.sub)
        assertEquals(payload.email, userInfo.email)
    }
}
