package com.millo.ollim.diary.repository

import com.millo.ollim.diary.domain.DiaryCollectionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DiaryCollectionsRepository: JpaRepository<DiaryCollectionEntity,UUID> {
    fun findAllByUserId(userId: UUID): List<DiaryCollectionEntity>
    fun findByCollectionId(collectionId: UUID): DiaryCollectionEntity
}
