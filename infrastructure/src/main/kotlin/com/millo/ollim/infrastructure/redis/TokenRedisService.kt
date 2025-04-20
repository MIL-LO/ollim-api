package com.millo.ollim.infrastructure.redis

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Redis를 활용한 Refresh Token 저장/조회/삭제 Service
 * - 저장 키: auth:refresh:{userId}
 * - TTL: 14일
 */
@Service
class TokenRedisService(
    private val redisTemplate: StringRedisTemplate
) {
    companion object {
        private const val PREFIX = "auth:refresh:"
        private const val TTL_SECONDS = 14 * 24 * 60 * 60L
    }

    /**
     * RefreshToken 저장
     * @param userId 사용자 UUID
     * @param token 발급된 RefreshToken
     */
    fun saveRefreshToken(userId: UUID, token: String) {
        val key = PREFIX + userId.toString()
        redisTemplate.opsForValue().set(key, token, TTL_SECONDS, TimeUnit.SECONDS)
    }

    /**
     * RefreshToken 조회
     * @param userId 사용자 UUID
     * @return 저장된 RefreshToken (없으면 null)
     */
    fun getRefreshToken(userId: UUID): String? {
        val key = PREFIX + userId.toString()
        return redisTemplate.opsForValue().get(key)
    }

    /**
     * RefreshToken 삭제 (로그아웃 시 사용)
     * @param userId 사용자 UUID
     */
    fun deleteRefreshToken(userId: UUID) {
        val key = PREFIX + userId.toString()
        redisTemplate.delete(key)
    }
}
