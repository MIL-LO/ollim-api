package com.millo.ollim

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.millo.ollim.core.domain"])
@EntityScan(basePackages = ["com.millo.ollim.core.domain"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
