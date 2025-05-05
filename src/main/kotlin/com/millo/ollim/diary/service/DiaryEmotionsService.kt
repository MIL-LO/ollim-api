package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryEmotionEntity
import com.millo.ollim.diary.domain.DiaryEntryEntity
import com.millo.ollim.diary.domain.EmotionTagEntity
import com.millo.ollim.diary.repository.DiaryEmotionsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DiaryEmotionsService(
    private val diaryEmotionsRepository: DiaryEmotionsRepository
){
    @Transactional
    fun save(diaryId: UUID, emotionTagIds: List<Int>): List<DiaryEmotionEntity> {
        val diaryProxy = DiaryEntryEntity(diaryId)
        return emotionTagIds.map { tagId ->
            val tagProxy = EmotionTagEntity(tagId)
            diaryEmotionsRepository.save(DiaryEmotionEntity(diaryProxy, tagProxy))
        }
    }

    @Transactional(readOnly = false)
    fun deleteByDiaryId(diaryId: UUID) {
        diaryEmotionsRepository.deleteAllByIdDiaryId(diaryId)
    }

    @Transactional(readOnly = false)
    fun deleteByTag(tagId: Int) {
        diaryEmotionsRepository.deleteAllByIdTagId(tagId)
    }
    @Transactional(readOnly = true)
    fun findByDiaryId(id: UUID):List<DiaryEmotionEntity> {
        return diaryEmotionsRepository.findAllByIdDiaryId(id)
    }
}
