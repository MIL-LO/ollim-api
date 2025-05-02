package com.millo.ollim.user.domain

import com.millo.ollim.common.domain.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.time.LocalDateTime

/**
 * 사용자 계정과 소셜 로그인 정보 매핑 테이블
 */
@Entity
@Table(name = "user_oauth")
class UserOAuthEntity(

    /**
     * 기본 키 (자동 증가)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    val id: Long = 0L,

    /**
     * FK → users.id, 사용자 계정 참조
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("FK → users.id, 사용자 계정 참조")
    val user: UserEntity,

    /**
     * 로그인 제공자 구분: GOOGLE 또는 APPLE
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 10)
    @Comment("로그인 제공자 구분: GOOGLE 또는 APPLE")
    val provider: ProviderType,

    /**
     * 소셜 로그인 플랫폼의 고유 사용자 ID
     */
    @Column(name = "oauth_id", nullable = false, length = 255)
    @Comment("소셜 로그인 플랫폼의 고유 사용자 ID")
    val oauthId: String,

    /**
     * OAuth 등록시간
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("등록 시각")
    val createdAt: LocalDateTime = LocalDateTime.now()

)
