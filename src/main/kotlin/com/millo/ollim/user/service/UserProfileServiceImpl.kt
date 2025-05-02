package com.millo.ollim.user.service

import com.millo.ollim.user.domain.UserProfileEntity
import com.millo.ollim.user.domain.UserStatus
import com.millo.ollim.user.dto.UserProfileDTO
import com.millo.ollim.user.repository.UserProfileRepository
import com.millo.ollim.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserProfileServiceImpl(
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository
) : UserProfileService {
    @Transactional
    override fun createOrUpdateProfile(userId: UUID, userProfileRequest: UserProfileDTO.UserProfileRequest) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다. id=$userId") }

        val profile = user.profile?.apply {
            nickname = userProfileRequest.nickname
            gender = userProfileRequest.gender
            birthDate = userProfileRequest.birthDate
            energyType = userProfileRequest.energyType
            activeTime = userProfileRequest.activeTime
            activitySpaces = userProfileRequest.activitySpaces
            mbti = userProfileRequest.mbti
            profileImage = userProfileRequest.profileImage
        } ?: UserProfileEntity(
            userId = userId,
            user = user,
            nickname = userProfileRequest.nickname,
            gender = userProfileRequest.gender,
            birthDate = userProfileRequest.birthDate,
            energyType = userProfileRequest.energyType,
            activeTime = userProfileRequest.activeTime,
            activitySpaces = userProfileRequest.activitySpaces,
            mbti = userProfileRequest.mbti,
            profileImage = userProfileRequest.profileImage
        )

        user.profile = profile

        // 상태가 PENDING일 때만 ACTIVE로 전환
        if (user.status == UserStatus.PENDING) {
            user.status = UserStatus.ACTIVE
        }

        userProfileRepository.save(profile)
        userRepository.save(user)
    }

}
