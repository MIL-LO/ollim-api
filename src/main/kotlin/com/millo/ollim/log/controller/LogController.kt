package com.millo.ollim.log.controller

import com.millo.ollim.log.service.LogTestService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/logTest")
class LogController(
    private val logTestService: LogTestService,
) {

    @GetMapping("/success")
    fun success() = logTestService.getSuccess( "test")
    @GetMapping("/fail")
    fun fail(): ResponseEntity<String> {
        try {
            logTestService.getFailure("test")
        }catch (e:RuntimeException){
            println("catch logTestService.getFailure")
        }
        return ResponseEntity.status( HttpStatus.OK).body("fail checked");
    }
}
