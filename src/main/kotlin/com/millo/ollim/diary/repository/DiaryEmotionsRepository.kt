package com.millo.ollim.diary.repository

import com.millo.ollim.diary.domain.DiaryEmotions
import com.millo.ollim.diary.domain.DiaryEntryEmotionId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DiaryEmotionsRepository : JpaRepository<DiaryEmotions, DiaryEntryEmotionId> {
    fun deleteAllByIdTagId(tagId: Int)
    fun deleteAllByIdDiaryId(diaryId: UUID)
    fun findAllByIdDiaryId(diaryId: UUID):List<DiaryEmotions>
}
