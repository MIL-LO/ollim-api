package com.millo.ollim.infrastructure.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisService(
    private val redisTemplate: RedisTemplate<String, String>
) {

    fun save(key: String, value: String) {
        redisTemplate.opsForValue().set(key, value)
    }

    fun get(key: String): String? {
        return redisTemplate.opsForValue().get(key)
    }

    fun delete(key: String) {
        redisTemplate.delete(key)
    }
}
