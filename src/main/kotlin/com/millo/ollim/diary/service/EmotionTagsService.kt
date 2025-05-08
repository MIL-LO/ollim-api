package com.millo.ollim.diary.service

import com.millo.ollim.diary.dto.TagDTO

interface EmotionTagsService {

    fun createNewTag(request: TagDTO.CreateRequest)
    fun getAllTags(): List<TagDTO.Response>
    fun updateTag(request: TagDTO.UpdateRequest)
    fun delete(tagId: Int)
}
