package com.millo.ollim.diary.repository

import com.millo.ollim.diary.domain.DiaryContents
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DiaryContentsRepository: MongoRepository<DiaryContents,String> {
    fun findAllByUserId(toString: String): MutableList<DiaryContents>
    fun findByUserId(toString: String): DiaryContents
}
