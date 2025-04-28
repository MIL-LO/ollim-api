package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryEmotions
import com.millo.ollim.diary.repository.DiaryEmotionsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DiaryEmotionsService(
    private val diaryEmotionsRepository: DiaryEmotionsRepository
){
    @Transactional(readOnly = true)
    fun save(diaryId: UUID, emotionTags: List<Int>): List<DiaryEmotions> {
        val diaryEmotions: MutableList<DiaryEmotions> = ArrayList()
        emotionTags.forEach { emotionTag ->
            diaryEmotions.add(diaryEmotionsRepository.save(DiaryEmotions(diaryId,emotionTag)))
        }
        return diaryEmotions
    }

    @Transactional(readOnly = false)
    fun delete(diaryId: UUID) {
        diaryEmotionsRepository.deleteByIdDiaryId(diaryId)
    }
}
