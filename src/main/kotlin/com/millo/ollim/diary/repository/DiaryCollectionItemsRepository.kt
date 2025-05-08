package com.millo.ollim.diary.repository

import com.millo.ollim.diary.domain.DiaryCollectionId
import com.millo.ollim.diary.domain.DiaryCollectionItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DiaryCollectionItemsRepository: JpaRepository<DiaryCollectionItemEntity, DiaryCollectionId> {
    fun deleteByIdDiaryId(diaryId: UUID)
    fun findAllByIdDiaryCollectionId(collectionId: UUID): List<DiaryCollectionItemEntity>
    fun findByIdDiaryCollectionIdAndIdDiaryId(collectionId: UUID, diaryId:UUID): DiaryCollectionItemEntity

}
