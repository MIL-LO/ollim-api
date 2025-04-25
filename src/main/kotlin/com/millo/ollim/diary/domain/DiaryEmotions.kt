package com.millo.ollim.diary.domain

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "diary_emotions")
data class DiaryEmotions (
    @EmbeddedId
    val id:DiaryEntryEmotionId,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,
    @Column(name = "deleted_at", nullable = false)
    val deletedAt: LocalDateTime,
) {
    constructor(id: UUID, tagId: Int) : this(
        DiaryEntryEmotionId(id,tagId),LocalDateTime.now(),LocalDateTime.now()
    )
}
