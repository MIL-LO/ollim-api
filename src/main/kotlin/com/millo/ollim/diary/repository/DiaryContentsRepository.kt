package com.millo.ollim.diary.repository

import com.millo.ollim.diary.domain.DiaryContentEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DiaryContentsRepository: MongoRepository<DiaryContentEntity,String> {
}
