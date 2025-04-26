package com.millo.ollim.diary.repository

import com.millo.ollim.diary.domain.DiaryEmotions
import com.millo.ollim.diary.domain.DiaryEntryEmotionId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DiaryEmotionsRepository : JpaRepository<DiaryEmotions, DiaryEntryEmotionId> {

}
