package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.EmotionTags
import com.millo.ollim.diary.repository.EmotionTagsRepository
import com.millo.ollim.diary.request.TagRequest
import org.springframework.stereotype.Service

@Service
class EmotionTagsService(
    private val emotionTagsRepository: EmotionTagsRepository,
) {
    fun createNewTag(request: TagRequest):String{
        if(emotionTagsRepository.existsByName(request.name))
            return "this tag already exists"
        val res = emotionTagsRepository.save(EmotionTags(request))
        return res.toString()
    }
}
