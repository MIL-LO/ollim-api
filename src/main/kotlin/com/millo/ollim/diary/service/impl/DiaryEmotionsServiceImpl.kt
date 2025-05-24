package com.millo.ollim.diary.service.impl

import com.millo.ollim.diary.domain.DiaryEmotionEntity
import com.millo.ollim.diary.domain.DiaryEntryEntity
import com.millo.ollim.diary.domain.EmotionTagEntity
import com.millo.ollim.diary.repository.DiaryEmotionsRepository
import com.millo.ollim.diary.service.DiaryEmotionsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class DiaryEmotionsServiceImpl(
    private val diaryEmotionsRepository: DiaryEmotionsRepository
): DiaryEmotionsService {

    @Transactional
    override fun save(diaryId: UUID, emotionTagIds: List<Int>): List<DiaryEmotionEntity> {
        val diaryProxy = DiaryEntryEntity(diaryId)
        return emotionTagIds.map { tagId ->
            val tagProxy = EmotionTagEntity(tagId)
            diaryEmotionsRepository.save(DiaryEmotionEntity(diaryProxy, tagProxy))
        }
    }

    @Transactional(readOnly = false)
    override fun deleteByDiaryId(diaryId: UUID) {
        diaryEmotionsRepository.deleteAllByIdDiaryId(diaryId)
    }

    @Transactional(readOnly = false)
    override fun deleteByTag(tagId: Int) {
        diaryEmotionsRepository.deleteAllByIdTagId(tagId)
    }

    @Transactional(readOnly = true)
    override fun findByDiaryId(id: UUID):List<DiaryEmotionEntity> {
        return diaryEmotionsRepository.findAllByIdDiaryId(id)
    }
}
