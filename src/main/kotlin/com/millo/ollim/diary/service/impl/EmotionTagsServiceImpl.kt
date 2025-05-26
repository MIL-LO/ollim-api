package com.millo.ollim.diary.service.impl

import com.millo.ollim.diary.domain.EmotionTagEntity
import com.millo.ollim.diary.dto.TagDTO
import com.millo.ollim.diary.repository.EmotionTagsRepository
import com.millo.ollim.diary.service.DiaryEmotionsService
import com.millo.ollim.diary.service.EmotionTagsService
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class EmotionTagsServiceImpl(
    private val emotionTagsRepository: EmotionTagsRepository,
    private val emotionsService: DiaryEmotionsService
): EmotionTagsService {

    // 태그명 중복 불가
    @Transactional(readOnly = false)
    override fun createNewTag(request: TagDTO.CreateRequest){
        if(emotionTagsRepository.existsByName(request.name))
            throw Exception("This tag name is already exists.")
        emotionTagsRepository.save(EmotionTagEntity(request))
    }

    // 감정 태그는 전체 조회 될 것이라는 가정
    @Transactional(readOnly = true)
    override fun getAllTags(): List<TagDTO.Response> {
        return emotionTagsRepository.findAll().map { tag-> TagDTO.Response(tag) }
    }

    @Transactional(readOnly = false)
    override fun updateTag(request: TagDTO.UpdateRequest) {
        if (!emotionTagsRepository.existsById(request.id))
            throw EntityNotFoundException()
        emotionTagsRepository.save(EmotionTagEntity(request))
    }

    // 삭제할 때, 다이어리와 연결된 태그 전부 제거 필요
    @Transactional(readOnly = false)
    override fun delete(tagId: Int) {
        emotionsService.deleteByTag(tagId)
        emotionTagsRepository.deleteAllById(tagId)
    }

}
