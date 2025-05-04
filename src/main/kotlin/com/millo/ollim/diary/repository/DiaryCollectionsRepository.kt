package com.millo.ollim.diary.repository

import com.millo.ollim.diary.domain.DiaryCollections
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DiaryCollectionsRepository: JpaRepository<DiaryCollections,UUID> {
    fun findAllByUserId(userId: UUID): List<DiaryCollections>
    fun findByCollectionId(collectionId: UUID): DiaryCollections
}
