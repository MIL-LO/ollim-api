package com.millo.ollim.diary.domain

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "diary_emotions")
data class DiaryEmotionEntity(
    @EmbeddedId
    val id: DiaryEntryEmotionId = DiaryEntryEmotionId(UUID.randomUUID(), 0),
    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_tag_id")
    val emotionTag: EmotionTagEntity,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted_at", nullable = false)
    val deletedAt: LocalDateTime = LocalDateTime.now()
) {
    // 생성자 추가: 편하게 만들 수 있게
    constructor(diary: DiaryEntryEntity, emotionTag: EmotionTagEntity) : this(
        id = DiaryEntryEmotionId(diary.id, emotionTag.id),
        emotionTag = emotionTag
    )
}
