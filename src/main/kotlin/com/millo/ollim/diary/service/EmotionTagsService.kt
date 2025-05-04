package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.EmotionTags
import com.millo.ollim.diary.repository.EmotionTagsRepository
import com.millo.ollim.diary.dto.TagDTO
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmotionTagsService(
    private val emotionTagsRepository: EmotionTagsRepository,
    private val emotionsService: DiaryEmotionsService
) {

    // 태그명 중복 불가
    @Transactional(readOnly = false)
    fun createNewTag(request: TagDTO.CreateRequest){
        if(emotionTagsRepository.existsByName(request.name))
            throw Exception("This tag name is already exists.")
        emotionTagsRepository.save(EmotionTags(request))
    }

    // 감정 태그는 전체 조회 될 것이라는 가정
    @Transactional(readOnly = true)
    fun getAllTags(): List<TagDTO.Response> {
        return emotionTagsRepository.findAll().map { tag-> TagDTO.Response(tag) }
    }

    @Transactional(readOnly = false)
    fun updateTag(request: TagDTO.UpdateRequest) {
        if (!emotionTagsRepository.existsById(request.id))
            throw EntityNotFoundException()
        emotionTagsRepository.save(EmotionTags(request))
    }

    // 삭제할 때, 다이어리와 연결된 태그 전부 제거 필요
    @Transactional(readOnly = false)
    fun delete(tagId: Int) {
        emotionsService.deleteByTag(tagId)
        emotionTagsRepository.deleteAllById(tagId)
    }

}
