package com.millo.ollim.diary.response

import com.millo.ollim.diary.domain.DiaryContents
import com.millo.ollim.diary.domain.DiaryEmotions
import com.millo.ollim.diary.domain.DiaryEntries
import java.util.UUID

data class DiaryVO(
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
