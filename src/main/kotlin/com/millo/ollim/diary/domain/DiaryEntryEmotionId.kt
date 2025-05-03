package com.millo.ollim.diary.domain

import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Embeddable
data class DiaryEntryEmotionId(
    @Column(name = "diary_id")
    val diaryId: UUID,

    @Column(name = "emotion_tag_id")
    val tagId: Int
) : Serializable
