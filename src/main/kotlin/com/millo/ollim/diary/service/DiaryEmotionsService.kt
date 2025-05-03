package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryEmotions
import com.millo.ollim.diary.domain.DiaryEntries
import com.millo.ollim.diary.domain.EmotionTags
import com.millo.ollim.diary.repository.DiaryEmotionsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DiaryEmotionsService(
    private val diaryEmotionsRepository: DiaryEmotionsRepository
){
    @Transactional
    fun save(diaryId: UUID, emotionTagIds: List<Int>): List<DiaryEmotions> {
        val diaryProxy = DiaryEntries(diaryId)
        return emotionTagIds.map { tagId ->
            val tagProxy = EmotionTags(tagId)
            diaryEmotionsRepository.save(DiaryEmotions(diaryProxy, tagProxy))
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
    fun findByDiaryId(id: UUID):List<DiaryEmotions> {
        return diaryEmotionsRepository.findAllByIdDiaryId(id)
    }
}
