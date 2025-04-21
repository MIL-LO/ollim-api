package com.millo.ollim.user.repository

import com.millo.ollim.user.domain.ProviderType
import com.millo.ollim.user.domain.UserOAuthEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * 사용자 OAuth 연동 정보에 대한 JPA Repository
 */
interface UserOAuthRepository : JpaRepository<UserOAuthEntity, Long> {

    /**
     * provider + oauthId 기준으로 사용자 OAuth 정보 조회
     */
    fun findByProviderAndOauthId(provider: ProviderType, oauthId: String): UserOAuthEntity?
}
