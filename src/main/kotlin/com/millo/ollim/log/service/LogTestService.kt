package com.millo.ollim.log.service

import org.springframework.stereotype.Service

@Service
open class LogTestService {

    open fun getSuccess(input:String):String{
        println("getSuccess: $input")
        return "Success: ${input}"
    }

    open fun getFailure(input:String):String{
        println("getFail"+input)
        throw RuntimeException("Failure: ${input}")
    }

}
