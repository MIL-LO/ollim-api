package com.millo.ollim.user.service

import com.millo.ollim.user.dto.UserProfileDTO
import java.util.*

/**
 * 사용자 프로필 관리
 */
interface UserProfileService {

    /**
     * 프로필 생성 또는 수정 (회원가입 시 최초 작성 혹은 이후는 수정)
     */
    fun createOrUpdateProfile(userId: UUID, userProfileRequest: UserProfileDTO.UserProfileRequest)

    /**
     * 사용자 프로필 조회 (마이페이지)
     */
    fun getProfile(userId: UUID): UserProfileDTO.UserProfileResponse

    /**
     * 회원 탈퇴
     */
    fun withdraw(userId: UUID)
}
