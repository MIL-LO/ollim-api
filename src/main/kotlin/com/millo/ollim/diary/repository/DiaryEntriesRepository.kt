package com.millo.ollim.diary.repository

import com.millo.ollim.diary.domain.DiaryEntries
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DiaryEntriesRepository: JpaRepository<DiaryEntries, UUID> {
    fun findAllById(id: UUID): MutableList<DiaryEntries>
    fun findAllByUserId(id: UUID): MutableList<DiaryEntries>
    fun findAllByUserIdOrderByCreatedAtDesc(id: UUID): MutableList<DiaryEntries>

}
