package com.millo.ollim.user.domain

import jakarta.persistence.*
import java.util.*

/**
 * 사용자 OAuth 연동 정보 Entity
 */
@Entity
@Table(name = "user_oauth")
class UserOAuthEntity(

    /**
     * 내부 ID (SERIAL)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0L,

    /**
     * users 테이블과 연관 (UUID FK)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    /**
     * 공급자 (GOOGLE, APPLE)
     */
    @Column(name = "provider", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val provider: ProviderType,

    /**
     * OAuth 공급자에서 제공하는 사용자 고유 ID
     */
    @Column(name = "oauth_id", nullable = false, length = 255)
    val oauthId: String
)
