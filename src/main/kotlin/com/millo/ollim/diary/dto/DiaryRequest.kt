package com.millo.ollim.diary.dto



data class DiaryRequest(
    val content: String,
    val imgUrl: String,
    val mood: String, // 무드 = 기분
    val emotionTags: List<Int>, // 이모션 = 닭감정
    ) {
}
