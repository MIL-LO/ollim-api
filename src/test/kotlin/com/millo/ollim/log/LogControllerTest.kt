package com.millo.ollim.log

import com.millo.ollim.log.controller.LogController
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import kotlin.test.Test

@WebMvcTest(LogController::class)
@ExtendWith(SpringExtension::class)
class LogControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var logController: LogController

    @Test
    fun testGetLogSuccess() {
        mockMvc.perform(get("/logTest/success"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun testGetLogFailure() {
        mockMvc.perform(get("/logTest/fail"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

}
