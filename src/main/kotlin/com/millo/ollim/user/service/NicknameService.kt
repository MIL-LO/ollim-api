package com.millo.ollim.user.service

import com.millo.ollim.user.dto.UserProfileDTO

/**
 * 닉네임 추천 기능을 위한 서비스 인터페이스
 */
interface NicknameService {

    /**
     * 감정 + 동물 이름 조합으로 랜덤 닉네임을 생성합니다.
     * 예: 기쁜너구리, 설렌호랑이
     *
     * @return 추천 닉네임 응답 DTO
     */
    fun generateNickname(): UserProfileDTO.NicknameResponse
}
