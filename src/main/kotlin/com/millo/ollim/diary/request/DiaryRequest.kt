package com.millo.ollim.diary.request

import jakarta.validation.constraints.NotBlank
import org.springframework.web.multipart.MultipartFile


data class NewDiaryRequest(
    val content: String,
    val imgUrl: MultipartFile?=null,
    val mood: String, // 무드 = 기분
    val emotionTags: List<Int>, // 이모션 = 닭감정
    ) {
}
