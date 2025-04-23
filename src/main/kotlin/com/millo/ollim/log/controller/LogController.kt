package com.millo.ollim.log.controller

import com.millo.ollim.log.service.LogTestService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/logTest")
class LogController(
    private val logTestService: LogTestService,
) {

    @GetMapping("/success")
    fun success() = logTestService.getSuccess("test")
    @GetMapping("/fail")
    fun fail() = logTestService.getFailure("test")
}
