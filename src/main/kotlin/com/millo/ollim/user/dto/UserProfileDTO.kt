package com.millo.ollim.user.dto

import com.millo.ollim.user.domain.EmotionCharacterType
import com.millo.ollim.user.domain.UserMBTI
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

/**
 * UserProfile 관련 DTO
 */
class UserProfileDTO {

    /**
     * 회원가입 시 사용자 프로필 요청
     */
    @Schema(description = "회원가입 시 작성하는 프로필 정보 요청 DTO")
    data class UserProfileRequest(
        @Schema(description = "사용자 닉네임", example = "행복한토끼")
        val nickname: String,

        @Schema(description = "성별 (예: MALE, FEMALE)", example = "MALE")
        val gender: String? = null,

        @Schema(description = "생년월일", example = "1997-01-04")
        val birthDate: LocalDate? = null,

        @Schema(description = "주로 활동하는 시간대", example = "NIGHT")
        val activeTime: String? = null,

        @Schema(description = "에너지 소비 유형 (예: INDOOR, OUTDOOR)", example = "OUTDOOR")
        val energyType: String? = null,

        @Schema(description = "주로 활동하는 공간 (예: HOME, CAFE)", example = "CAFE")
        val activitySpaces: String? = null,

        @Schema(description = "MBTI 성격 유형", example = "INFP")
        val mbti: UserMBTI? = null,

        @Schema(description = "감정 캐릭터 이미지", example = "CHEERFUL_CAT")
        val profileImage: EmotionCharacterType? = null
    )

    /**
     * 닉네임 추천 응답 DTO
     */
    @Schema(description = "랜덤 닉네임 추천 응답 DTO")
    data class NicknameResponse(
        @Schema(description = "추천된 닉네임", example = "신나는다람쥐")
        val nickname: String
    )
}
