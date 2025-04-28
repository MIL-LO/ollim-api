package com.millo.ollim.diary.repository

import com.millo.ollim.diary.domain.DiaryCollectionId
import com.millo.ollim.diary.domain.DiaryCollectionItems
import com.millo.ollim.diary.request.CollectionRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DiaryCollectionItemsRepository: JpaRepository<DiaryCollectionItems, DiaryCollectionId> {
    fun findByIdDiaryCollectionId(collectionId: UUID): MutableList<DiaryCollectionItems>
    fun findAllByIdDiaryCollectionId(collectionId: UUID): List<DiaryCollectionItems>
    fun deleteByIdDiaryId(diaryId: UUID)
}
