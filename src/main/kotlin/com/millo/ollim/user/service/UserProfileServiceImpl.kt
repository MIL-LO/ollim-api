package com.millo.ollim.user.service

import com.millo.ollim.user.domain.UserProfileEntity
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
    override fun createOrUpdateProfile(userId: UUID, request: UserProfileDTO.Request) {
        val user = userRepository.findById(userId)
            .orElseThrow{IllegalArgumentException("사용자를 찾을 수 없습니다. id=$userId")}

        val profile = user.profile?.apply {
            nickname = request.nickname
            gender = request.gender
            birthDate = request.birthDate
            energyType = request.energyType
            activeTime = request.activeTime
            activitySpaces = request.activitySpaces
            mbti = request.mbti
            profileImage = request.profileImage
        } ?: UserProfileEntity(
            userId = userId,
            user = user,
            nickname = request.nickname,
            gender = request.gender,
            birthDate = request.birthDate,
            energyType = request.energyType,
            activeTime = request.activeTime,
            activitySpaces = request.activitySpaces,
            mbti = request.mbti,
            profileImage = request.profileImage
        )

        user.profile = profile
        userProfileRepository.save(profile)
    }

}
