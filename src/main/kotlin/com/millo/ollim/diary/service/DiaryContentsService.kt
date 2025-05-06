package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryContentEntity
import com.millo.ollim.diary.domain.DiaryEntryEntity
import java.util.*

interface DiaryContentsService {

    fun save(diaryEntriesEntity: DiaryEntryEntity, contents: String, imgUrl: String): DiaryContentEntity
    fun findByDiaryId(id: UUID): DiaryContentEntity
    fun delete(diaryId: UUID)
    fun update(id: UUID, content: String, imgUrl: String): DiaryContentEntity
}
