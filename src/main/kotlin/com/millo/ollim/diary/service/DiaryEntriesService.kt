package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryEntryEntity
import org.springframework.data.domain.PageRequest
import java.util.*

interface DiaryEntriesService {

    fun save(diaryEntriesEntity: DiaryEntryEntity): DiaryEntryEntity
    fun update(diaryEntriesEntity: DiaryEntryEntity): DiaryEntryEntity
    fun delete(id: UUID)
    fun findByUserIdWithIsNotDeleted(id: UUID, pageRequest: PageRequest): MutableList<DiaryEntryEntity>
    fun findById(diaryId: UUID): DiaryEntryEntity
}
