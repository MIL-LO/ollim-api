package com.millo.ollim.app

import io.sentry.Sentry
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment

@SpringBootTest
class SentryIntegrationTest {

    @Test
    fun `Sentry에 예외 전송 테스트`() {
        try {
            throw Exception("Sentry app-api 테스트 예외입니다.")
        } catch (e: Exception) {
            Sentry.captureException(e)
        }
    }
}
