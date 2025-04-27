package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.EmotionTags
import com.millo.ollim.diary.repository.EmotionTagsRepository
import com.millo.ollim.diary.request.TagRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmotionTagsService(
    private val emotionTagsRepository: EmotionTagsRepository,
) {
    @Transactional
    fun createNewTag(request: TagRequest):String{
        if(emotionTagsRepository.existsByName(request.name))
            return "this tag already exists"
        val res = emotionTagsRepository.save(EmotionTags(request))
        return res.toString()
    }

    @Transactional
    fun getTags(): List<EmotionTags> {
        return emotionTagsRepository.findAll()
    }

}
