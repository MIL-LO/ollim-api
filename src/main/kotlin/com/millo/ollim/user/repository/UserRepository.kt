package com.millo.ollim.user.repository

import com.millo.ollim.user.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * 사용자(User) 엔티티에 대한 JPA Repository
 */
interface UserRepository : JpaRepository<UserEntity, UUID> {

    /**
     * 이메일 기준으로 사용자 조회
     */
    fun findByEmail(email: String): UserEntity?

    /**
     * 탈퇴하지 않은 계정만 조회
     */
    fun findByEmailAndWithdrawnAtIsNull(email: String): UserEntity?
}
