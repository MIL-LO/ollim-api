package com.millo.ollim.log.service

import org.springframework.stereotype.Service

@Service
class LogTestService {

    fun getSuccess(input:String):String{
        println("getSuccess: $input")
        return "Success: ${input}"
    }

    fun getFailure(input:String):String{
        println("getFail"+input)
        throw RuntimeException("Failure: ${input}")
    }

}
