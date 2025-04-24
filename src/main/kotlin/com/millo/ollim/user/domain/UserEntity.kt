package com.millo.ollim.user.domain

import com.millo.ollim.common.domain.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.time.LocalDateTime
import java.util.*

/**
 * 소셜 로그인 기반 사용자 계정 정보
 */
@Entity
@Table(name = "users")
class UserEntity(

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @Comment("사용자 고유 식별자 (Primary Key)")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "email", nullable = false, unique = true, length = 255)
    @Comment("로그인에 사용되는 이메일, 중복 불가")
    val email: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    @Comment("계정 역할: 일반 사용자(USER) 또는 관리자(ADMIN)")
    val role: UserRole = UserRole.USER,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Comment("계정 상태: ACTIVE(정상), WITHDRAWN(탈퇴) 등")
    val status: UserStatus = UserStatus.ACTIVE,

    @Column(name = "last_login_at")
    @Comment("마지막 로그인 시각 (관리자 포함)")
    var lastLoginAt: LocalDateTime? = null,

    @Column(name = "withdrawn_at")
    @Comment("계정 탈퇴 처리 일시 (soft delete 용도)")
    val withdrawnAt: LocalDateTime? = null,

    /**
     * 사용자 프로필 - 1:1 관계, 사용자 저장 시 자동 cascade
     */
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var profile: UserProfileEntity? = null

) : BaseTimeEntity()
