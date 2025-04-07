package com.millo.ollim.core.domain.user.entity

import com.millo.ollim.core.common.entity.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class Users(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val email: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role = Role.USER,

    @Column(nullable = false)
    val isActive: Boolean = true,

    val withdrawnAt: LocalDateTime? = null
) : BaseTimeEntity()

enum class Role {
    USER, ADMIN
}
