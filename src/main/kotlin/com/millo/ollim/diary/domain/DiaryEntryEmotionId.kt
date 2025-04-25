package com.millo.ollim.diary.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.*

// 프로토타입 개발 이후 필요 시 사용 예정
@Embeddable
data class DiaryEntryEmotionId(
    @Column(name = "diary_id", nullable = false)
    val diaryId: UUID,
    @Column(name = "emotion_tag_id", nullable = false)
    private val tagId: Int) : Serializable
{

}
