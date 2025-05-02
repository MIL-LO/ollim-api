package com.millo.ollim.user.service

import com.millo.ollim.user.domain.UserProfileEntity
import com.millo.ollim.user.domain.UserStatus
import com.millo.ollim.user.dto.UserProfileDTO
import com.millo.ollim.user.repository.UserProfileRepository
import com.millo.ollim.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

/**
 * 사용자 프로필 관련 서비스 구현체
 */
@Service
class UserProfileServiceImpl(
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository
) : UserProfileService {

    /**
     * 프로필 생성 또는 수정
     * - PENDING 상태에서는 최초 생성만 가능
     * - ACTIVE 상태에서는 수정만 가능
     * - WITHDRAWN 상태는 차단
     */
    @Transactional
    override fun createOrUpdateProfile(userId: UUID, userProfileRequest: UserProfileDTO.UserProfileRequest) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다. id=$userId") }

        when (user.status) {
            UserStatus.PENDING -> {
                if (user.profile != null) {
                    throw IllegalStateException("이미 프로필이 존재합니다. 수정은 ACTIVE 상태에서만 가능합니다.")
                }

                val profile = UserProfileEntity(
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
                user.status = UserStatus.ACTIVE

                userProfileRepository.save(profile)
                userRepository.save(user)
            }

            UserStatus.ACTIVE -> {
                val profile = user.profile
                    ?: throw IllegalStateException("수정할 프로필이 존재하지 않습니다. userId=$userId")

                profile.nickname = userProfileRequest.nickname
                profile.gender = userProfileRequest.gender
                profile.birthDate = userProfileRequest.birthDate
                profile.energyType = userProfileRequest.energyType
                profile.activeTime = userProfileRequest.activeTime
                profile.activitySpaces = userProfileRequest.activitySpaces
                profile.mbti = userProfileRequest.mbti
                profile.profileImage = userProfileRequest.profileImage

                userProfileRepository.save(profile)
            }

            UserStatus.WITHDRAWN -> {
                throw IllegalStateException("탈퇴한 사용자는 프로필을 수정할 수 없습니다. userId=$userId")
            }
        }
    }

    /**
     * 사용자 프로필 조회 (마이페이지 용도)
     */
    @Transactional(readOnly = true)
    override fun getProfile(userId: UUID): UserProfileDTO.UserProfileResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다. id=$userId") }

        if (user.status == UserStatus.WITHDRAWN) {
            throw IllegalStateException("탈퇴한 사용자는 정보를 조회할 수 없습니다. userId=$userId")
        }

        val profile = user.profile
            ?: throw IllegalStateException("프로필 정보가 없습니다. userId=$userId")

        return UserProfileDTO.UserProfileResponse(
            nickname = profile.nickname,
            gender = profile.gender,
            birthDate = profile.birthDate,
            energyType = profile.energyType,
            activeTime = profile.activeTime,
            activitySpaces = profile.activitySpaces,
            mbti = profile.mbti,
            profileImage = profile.profileImage
        )
    }

    @Transactional
    override fun withdraw(userId: UUID) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다. id=$userId") }

        if (user.status == UserStatus.WITHDRAWN) {
            throw IllegalStateException("이미 탈퇴한 사용자입니다.")
        }

        user.status = UserStatus.WITHDRAWN
        user.withdrawnAt = LocalDateTime.now()

        userRepository.save(user)
    }
}
