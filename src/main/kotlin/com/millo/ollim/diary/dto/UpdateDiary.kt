package com.millo.ollim.diary.dto

import java.util.UUID

data class UpdateDiary(
    val diaryId: UUID,
    val content: String,
    val imgUrl: String,
    val mood: String, // 무드 = 기분
    val emotionTags: List<Int>, // 이모션 = 감정
) {

}
