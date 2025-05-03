package com.millo.ollim.diary.repository

import com.millo.ollim.diary.domain.DiaryCollectionId
import com.millo.ollim.diary.domain.DiaryCollectionItems
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.swing.SortOrder

@Repository
interface DiaryCollectionItemsRepository: JpaRepository<DiaryCollectionItems, DiaryCollectionId> {
    fun findByIdDiaryCollectionId(collectionId: UUID): MutableList<DiaryCollectionItems>
    fun findAllByIdDiaryCollectionId(collectionId: UUID, page: PageRequest): List<DiaryCollectionItems>
    fun deleteByIdDiaryId(diaryId: UUID)
    fun findAllByIdDiaryCollectionId(collectionId: UUID): List<DiaryCollectionItems>
    fun findByIdDiaryCollectionIdAndIdDiaryId(collectionId: UUID, diaryId:UUID): DiaryCollectionItems

    @Modifying
    @Query("UPDATE DiaryCollectionItems dci SET dci.sortOrder = :sortOrder WHERE dci.id.diaryId = :diaryId AND dci.id.diaryCollectionId = :collectionId")
    fun updateSort(@Param("diaryId") diaryId: UUID, @Param("sortOrder") sortOrder: Int, @Param("collectionId") collectionId: UUID)

}
