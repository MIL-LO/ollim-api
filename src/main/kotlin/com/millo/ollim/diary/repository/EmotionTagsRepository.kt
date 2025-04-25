package com.millo.ollim.diary.repository

import com.millo.ollim.diary.domain.EmotionTags
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmotionTagsRepository: JpaRepository<EmotionTags, Int> {
    fun existsByName(tag: String): Boolean

}
