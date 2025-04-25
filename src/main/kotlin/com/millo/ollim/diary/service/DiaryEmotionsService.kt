package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryEmotions
import com.millo.ollim.diary.repository.DiaryEmotionsRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class DiaryEmotionsService(
    private val diaryEmotionsRepository: DiaryEmotionsRepository
){
    fun save(diaryId: UUID, emotionTags: List<Int>): List<DiaryEmotions> {
        val diaryEmotions: MutableList<DiaryEmotions> = ArrayList()
        emotionTags.forEach { emotionTag ->
            diaryEmotions.add(diaryEmotionsRepository.save(DiaryEmotions(diaryId,emotionTag)))
        }
        return diaryEmotions
    }
}
