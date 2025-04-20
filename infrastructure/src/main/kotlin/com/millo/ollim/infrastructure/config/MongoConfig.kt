package com.millo.ollim.infrastructure.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.millo.ollim"])
class MongoConfig(
    @Value("\${spring.data.mongodb.host}") private val host: String,
    @Value("\${spring.data.mongodb.port}") private val port: Int,
    @Value("\${spring.data.mongodb.username}") private val username: String,
    @Value("\${spring.data.mongodb.password}") private val password: String,
    @Value("\${spring.data.mongodb.database}") private val database: String,
    @Value("\${spring.data.mongodb.authentication-database}") private val authDatabase: String
) {

    @Bean
    fun mongoClient(): MongoClient {
        val connectionString =
            "mongodb://$username:$password@$host:$port/$database?authSource=$authDatabase"
        return MongoClients.create(connectionString)
    }
    @Bean
    fun mongoTemplate(): MongoTemplate {
        return MongoTemplate(mongoClient(), database)
    }
}
