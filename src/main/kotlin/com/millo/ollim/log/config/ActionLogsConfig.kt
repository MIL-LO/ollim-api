package com.millo.ollim.log.config

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import org.springframework.stereotype.Component

// 모든 요청 처리에 대한 로그를 저장하기 위한 로직입니다.
// Spring Security 도입 이후 적용 예정
// 이유: 사용자/관리자 구분이 필요함
@Aspect
@Component
class ActionLogsConfig {

    @Pointcut("execution(* com.millo.ollim.log.*.*(..))")
    fun logMethods() {}

    @Before("logMethods()")
    fun beforeLogMethodExecution(joinPoint: JoinPoint) {
        val methodName = joinPoint.signature.name
        val args = joinPoint.args
        println("Before ${methodName}(${args.joinToString(", ")})")
    }

    @AfterReturning("logMethods()")
    fun afterLogMethodExecution(joinPoint: JoinPoint) {
        val methodName = joinPoint.signature.name
        val args = joinPoint.args
        println("After ${methodName}(${args.joinToString(", ")})")
    }

    @AfterThrowing("logMethods()")
    fun afterThrowingLogMethodExecution(joinPoint: JoinPoint): Boolean {
        val methodName = joinPoint.signature.name
        val args = joinPoint.args
        println("After Throwing ${methodName}(${args.joinToString(", ")})")

        return false
    }

    @Around("logMethods()")
    fun aroundLog(joinPoint: ProceedingJoinPoint): Any? {
        val startTime = System.currentTimeMillis()
        val result = joinPoint.proceed()
        val endTime = System.currentTimeMillis() - startTime
        println("Around endTime = ${endTime}")
        return result
    }

    fun saveLog(name:String, methodName:String, isAdmin:Boolean, actionType:String ) {

    }

    fun getUserInfo():String{
        // 사용자 정보 어떻게 가져오는지 확인 필요
        // 시큐리티 적용하면 SecurityContextHolder
        return "user"
    }
}
