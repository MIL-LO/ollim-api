package com.millo.ollim.diary.repository

import com.millo.ollim.diary.domain.DiaryEntryEntity
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DiaryEntriesRepository: JpaRepository<DiaryEntryEntity, UUID> {
    fun findAllByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(id: UUID, pageRequest: PageRequest): MutableList<DiaryEntryEntity>
}
