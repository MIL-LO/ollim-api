package com.millo.ollim.user.dto

import com.millo.ollim.user.domain.EmotionCharacterType
import java.time.LocalDate

/**
 * UserProfile작성폼
 */
class UserProfileDTO {

    data class Request(
    val nickname: String,
    val gender: String? = null,
    val birthDate: LocalDate? = null,
    val activeTime: String? = null,
    val energyType: String? = null,
    val activitySpaces: String? = null,
    val mbti: String? = null,
    val profileImage: EmotionCharacterType? = null
    //TODO: prifileImage는 아직 추가안했지만 추후 추가해서 ENUM으로 관리해야할듯?
    )
}

