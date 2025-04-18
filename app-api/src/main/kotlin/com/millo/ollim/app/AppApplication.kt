package com.millo.ollim.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.millo.ollim.core.domain"])
@EntityScan(basePackages = ["com.millo.ollim.core.domain"])
class AppApplication

fun main(args: Array<String>) {
    runApplication<AppApplication>(*args)
}
