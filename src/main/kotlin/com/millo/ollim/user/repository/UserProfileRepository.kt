package com.millo.ollim.user.repository

import com.millo.ollim.user.domain.UserProfileEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * 사용자 프로필 정보를 조회 및 저장하는 Repository
 */
interface UserProfileRepository : JpaRepository<UserProfileEntity, UUID> {

}
