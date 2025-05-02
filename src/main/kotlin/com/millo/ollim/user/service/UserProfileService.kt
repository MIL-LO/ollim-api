package com.millo.ollim.user.service

import com.millo.ollim.user.dto.UserProfileDTO
import java.util.*

interface UserProfileService {
    fun createOrUpdateProfile(userId: UUID, request: UserProfileDTO.Request)
}
