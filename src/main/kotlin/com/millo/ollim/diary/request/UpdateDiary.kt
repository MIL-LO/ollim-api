package com.millo.ollim.diary.request

import jakarta.persistence.Column
import jakarta.persistence.Id
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.UUID

data class UpdateDiary(
    val diaryId: UUID,
    val content: String,
    val imgUrl: MultipartFile?=null,
    val mood: String, // 무드 = 기분
    val emotionTags: List<Int>, // 이모션 = 감정
) {

}
