package com.millo.ollim.diary.domain

import com.millo.ollim.diary.request.UpdateDiary
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "diary_entries")
data class DiaryEntries(
    @Id
    @Column(name = "id", nullable = false)
    val id: UUID,
    @Column(name = "user_id", nullable = false)
    val userId: UUID,
    @Column(name = "mood", nullable = false)
    val mood: String,
    @Column(name = "emotion_tag", nullable = false)
    val emotionTag: String,
    @Column(name = "is_deleted", nullable = false)
    val isDeleted: Boolean,
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,
){
    constructor(userId: UUID,mood: String, emotionTag: String) : this(
        UUID.randomUUID(), userId,
        mood, emotionTag, false,
        LocalDateTime.now(), LocalDateTime.now())

    constructor(userId:UUID, updateDiary: UpdateDiary,isDeleted: Boolean, createdAt: LocalDateTime, updatedAt: LocalDateTime) : this(
        updateDiary.diaryId, userId,
        updateDiary.mood,updateDiary.emotionTags.toString(),
        isDeleted, createdAt, updatedAt
        )
}
