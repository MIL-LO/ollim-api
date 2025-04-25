package com.millo.ollim.diary.service

import com.millo.ollim.diary.repository.EmotionTagsRepository
import org.springframework.stereotype.Service

@Service
class EmotionTagsService(
    private val emotionTagsRepository: EmotionTagsRepository,
) {

}
