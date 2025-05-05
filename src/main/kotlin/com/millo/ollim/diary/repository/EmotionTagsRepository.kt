package com.millo.ollim.diary.repository

import com.millo.ollim.diary.domain.EmotionTagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmotionTagsRepository: JpaRepository<EmotionTagEntity, Int> {
    fun existsByName(tag: String): Boolean

    fun deleteAllById(tagId: Int)

}
