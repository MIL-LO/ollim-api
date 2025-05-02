package com.millo.ollim.user.service

import com.millo.ollim.user.dto.UserProfileDTO
import org.springframework.stereotype.Service

/**
 * 감정 + 동물 이름 조합으로 닉네임을 생성하는 서비스 구현체
 */
@Service
class NicknameServiceImpl : NicknameService {

    private val emotions = listOf(
        "기쁜", "설렌", "신난", "즐거운", "화난", "우울한",
        "당황한", "평온한", "뿌듯한", "감동한", "짜증난", "차분한"
    )

    private val animals = listOf(
        "고양이", "강아지", "너구리", "여우", "사자", "호랑이",
        "코끼리", "토끼", "다람쥐", "햄스터", "하마", "펭귄",
        "두더지", "기린", "곰", "판다", "앵무새", "고래"
    )

    override fun generateNickname(): UserProfileDTO.NicknameResponse {
        val emotion = emotions.random()
        val animal = animals.random()
        val nickname = "$emotion$animal"
        return UserProfileDTO.NicknameResponse(nickname)
    }
}
