//package com.millo.ollim.log
//
//import com.millo.ollim.log.service.LogTestService
//import com.mongodb.assertions.Assertions.assertTrue
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.system.CapturedOutput
//import org.springframework.boot.test.system.OutputCaptureExtension
//
//
//@SpringBootTest
//@ExtendWith(OutputCaptureExtension::class)
//class LogTest {
//
//    @Autowired
//    private lateinit var service: LogTestService
//
//    @Test
//    fun getSuccess(output: CapturedOutput) {
//
//        // given
//        val input = "test"
//        val expectResult = "Success: ${input}"
//
//        // when
//        val result = service.getSuccess(input)
//        println("result = ${result}")
//        // then
//        val printLog = output.out
//
//        assertEquals(expectResult, result)
//        assertTrue(printLog.contains("After getSuccess(${input})"))
//        assertTrue(printLog.contains("Before getSuccess(${input})"))
//        assertTrue(printLog.contains("Around endTime"))
//    }
//
//    @Test
//    fun getFail(output: CapturedOutput) {
//
//        // given
//        val input = "test"
//
//        // when
//        val exception = Assertions.assertThrows(RuntimeException::class.java) {
//            service.getFailure(input)
//        }
//
//        //then
//        val printLog = output.out
//        assertEquals(exception.message, "Failure: ${input}")
//        assertTrue(printLog.contains("Before getFailure(${input})"))
//        assertTrue(printLog.contains("After Throwing getFailure(${input})"))
//    }
//
//
//}
