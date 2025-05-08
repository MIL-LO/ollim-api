package com.millo.ollim.diary.domain

import com.millo.ollim.common.domain.BaseTimeEntity
import com.millo.ollim.diary.dto.DiaryDTO
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "diary_entries")
data class DiaryEntryEntity(
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
): BaseTimeEntity()
{
    constructor(userId: UUID,mood: String, emotionTag: String) : this(
        UUID.randomUUID(), userId,
        mood, emotionTag, false)

    constructor(userId:UUID, updateDiary: DiaryDTO.UpdateRequest,isDeleted: Boolean) : this(
        updateDiary.diaryId, userId,
        updateDiary.mood,updateDiary.emotionTags.toString(),
        isDeleted)

    constructor(diaryId: UUID) : this(
        diaryId, diaryId,
        "","",
        false)
}
