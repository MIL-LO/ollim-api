package com.millo.ollim.diary.dto

import com.millo.ollim.diary.domain.DiaryContents
import com.millo.ollim.diary.domain.DiaryEmotions
import com.millo.ollim.diary.domain.DiaryEntries
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

class DiaryDTO{

    @Schema(description = "다이어리 생성요청")
    data class CreateRequest(
        @Schema(description = "다이어리 내용", example = "오늘 날씨가 매우 좋았다.")
        val content: String,
        @Schema(description = "이미지 파일")
        val imgUrl: String,
        @Schema(description = "기분", example = "좋음")
        val mood: String,
        @Schema(description = "선택한 감정 목록", example = "[1,2,3]")
        val emotionTags: List<Int>,
    )

    data class UpdateRequest(
        val diaryId: UUID,
        @Schema(description = "다이어리 내용", example = "오늘 날씨가 매우 좋았다.")
        val content: String,
        @Schema(description = "이미지 파일")
        val imgUrl: String,
        @Schema(description = "기분", example = "좋음")
        val mood: String,
        @Schema(description = "선택한 감정 목록", example = "[1,2,3]")
        val emotionTags: List<Int>,
    )

    data class DiaryResponse(
        val id: UUID,
        val content: String,
        val imgUrl: String,
        val mood: String,
        val emotionsTags: List<EmotionTag>
    ){
        constructor(diaryEntries: DiaryEntries, diaryContents: DiaryContents, diaryEmotions: List<DiaryEmotions>):this(
            diaryEntries.id, diaryContents.content, diaryContents.imageUrl, diaryEntries.mood,
            diaryEmotions.map{ EmotionTag(it.id.tagId,it.emotionTag.id, it.emotionTag.name)}
        )
    }

    data class EmotionTag(
        val diaryEmotionTagId: Int,
        val emotionTagId:Int,
        val name: String
    )

}
