package com.millo.ollim.diary.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "diary_emotions")
data class DiaryEmotions (
    @Id
    @Column(name = "diary_id", nullable = false)
    val diaryId: UUID,
    @Column(name = "emotion_tag_id", nullable = false)
    val emotion_tag_id:Int,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Date,
    @Column(name = "deleted_at", nullable = false)
    val deletedAt: Date,
)
