package com.millo.ollim.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun redisConnectionFactory(
        @Value("\${database.redis.host}") host: String,
        @Value("\${database.redis.port}") port: Int,
        @Value("\${database.redis.password:}") password: String,
        @Value("\${database.redis.database:0}") database: Int
    ): LettuceConnectionFactory {
        val config = RedisStandaloneConfiguration(host, port).apply {
            this.database = database
            if (password.isNotBlank()) this.setPassword(password)
        }
        return LettuceConnectionFactory(config)
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: LettuceConnectionFactory): RedisTemplate<String, String> {
        return RedisTemplate<String, String>().apply {
            setConnectionFactory(redisConnectionFactory)
            keySerializer = StringRedisSerializer()
            valueSerializer = StringRedisSerializer()
        }
    }
}
