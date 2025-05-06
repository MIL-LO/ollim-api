package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryEmotionEntity
import java.util.*

interface DiaryEmotionsService {

    fun save(diaryId: UUID, emotionTagIds: List<Int>): List<DiaryEmotionEntity>
    fun deleteByDiaryId(diaryId: UUID)
    fun deleteByTag(tagId: Int)
    fun findByDiaryId(id: UUID): List<DiaryEmotionEntity>
}
