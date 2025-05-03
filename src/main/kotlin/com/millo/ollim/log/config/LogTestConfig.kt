package com.millo.ollim.log.config

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import org.springframework.stereotype.Component


@Aspect
@Component
class LogTestConfig {

    // 메서드 실행 시점을 기준으로 정의
    // (실행시점(모든타입_클래스_메서드_파라미터 ))
    @Pointcut(
        "execution(* com.millo.ollim.health.service..*.*(..)) ||" +
        "execution(* com.millo.ollim.log.service..*.*(..))" )
    fun logMethods() {}

    // Advice - return void
    // 1. Before
    // 실행 시점 전
    @Before("logMethods()")
    fun beforeLogMethodExecution(joinPoint: JoinPoint) {

        val methodName = joinPoint.signature.name
        val args = joinPoint.args
        println("Before ${methodName}(${args.joinToString(", ")})")
    }

    // 2. AfterReturning - return void
    // 예외 없이 정상 실행 시점 후
    @AfterReturning("logMethods()")
    fun afterLogMethodExecution(joinPoint: JoinPoint) {

        val methodName = joinPoint.signature.name
        val args = joinPoint.args
        println("After ${methodName}(${args.joinToString(", ")})")
    }

    // 3. AfterThrowing - return void
    // 예외 발생한 경우
    @AfterThrowing("logMethods()")
    fun afterThrowingLogMethodExecution(joinPoint: JoinPoint): Boolean {

        val methodName = joinPoint.signature.name
        val args = joinPoint.args
        println("After Throwing ${methodName}(${args.joinToString(", ")})")

        return false
    }

    // 4. Around: 메소드 호출 자체를 감싸서, 실행 전후처리 + 메서드 호출 제어 가능
    // 특정 조건에 따라 메소드 호출 차단 가능
    @Around("logMethods()")
    fun aroundLog(joinPoint: ProceedingJoinPoint): Any? {

        val startTime = System.currentTimeMillis()
        // proceed
//        if(startTime%2==0L)
        val result = joinPoint.proceed()
        val endTime = System.currentTimeMillis() - startTime
        println("Around endTime = ${endTime}")
        return result
    }
}
