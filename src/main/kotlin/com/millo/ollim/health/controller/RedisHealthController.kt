package com.millo.ollim.health.controller

import com.millo.ollim.health.service.RedisService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/redis")
class RedisHealthController(
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
