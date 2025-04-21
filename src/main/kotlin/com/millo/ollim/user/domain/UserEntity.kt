package com.millo.ollim.user.domain

import com.millo.ollim.common.domain.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

/**
 * User Entity
 */
@Entity
@Table(name = "users")
class UserEntity(

    /**
     * 사용자 고유 ID (UUID)
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    val id: UUID = UUID.randomUUID(),

    /**
     * 이메일 (로그인 시 사용됨)
     */
    @Column(name = "email", nullable = false, unique = true, length = 255)
    val email: String,

    /**
     * 사용자 역할 (USER, ADMIN)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    val role: UserRole = UserRole.USER,

    /**
     * 사용자 계정 상태 (ACTIVE, WITHDRAWN)
     */
    @Column(name = "status", nullable = false, length = 20)
    val status: String = "ACTIVE",

    /**
     * 마지막 로그인 시간
     */
    @Column(name = "last_login_at")
    val lastLoginAt: LocalDateTime? = null,

    /**
     * 탈퇴일시 (soft delete용)
     */
    @Column(name = "withdrawn_at")
    val withdrawnAt: LocalDateTime? = null

) : BaseTimeEntity()
