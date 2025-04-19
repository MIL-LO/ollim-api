package com.millo.ollim.controller

import com.millo.ollim.infrastructure.redis.RedisService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/redis")
class TestRedisController(
    private val redisService: RedisService
) {
    @PostMapping("/save")
    fun save(@RequestParam key: String,
             @RequestParam value: String): String {
        redisService.save(key, value)
        return "Saved: $key -> $value"
    }

    @GetMapping("/get")
    fun get(@RequestParam key: String): String? {
        return redisService.get(key) ?: "Not found"
    }

    @DeleteMapping("/delete")
    fun delete(@RequestParam key: String): String {
        redisService.delete(key)
        return "Deleted: $key"
    }
}
