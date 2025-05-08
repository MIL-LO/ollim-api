package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryEntryEntity
import com.millo.ollim.diary.dto.DiaryDTO
import java.util.*

interface DiaryService {
    fun createNewDiary(userId: UUID, newCreateRequest: DiaryDTO.CreateRequest): DiaryDTO.DiaryResponse
    fun getDiaries(userId: UUID, pageNum: Int): List<DiaryDTO.DiaryResponse>
    fun getDiaryResponse(diaryEntry: DiaryEntryEntity): DiaryDTO.DiaryResponse
    fun updateDiary(userId: UUID, updateDiary: DiaryDTO.UpdateRequest): DiaryDTO.DiaryResponse
    fun getDiary(userId: UUID, diaryId: UUID): DiaryDTO.DiaryResponse
    fun deleteDiary(userId: UUID, diaryId: UUID)
}
