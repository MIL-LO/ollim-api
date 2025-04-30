package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryEntries
import com.millo.ollim.diary.repository.DiaryEntriesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DiaryEntriesService (@Autowired val diaryEntriesRepository: DiaryEntriesRepository){

    @Transactional(readOnly = false)
    fun save(diaryEntries: DiaryEntries) = diaryEntriesRepository.save(diaryEntries)
    @Transactional(readOnly = false)
    fun update(diaryEntries: DiaryEntries) = diaryEntriesRepository.save(diaryEntries)
    @Transactional(readOnly = false)
    fun delete(id: UUID) = diaryEntriesRepository.deleteById(id)

    @Transactional(readOnly = true)
    fun findByUserIdWithIsNotDeleted(id: UUID) = diaryEntriesRepository.findAllByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(id)

    @Transactional
    fun findById(diaryId: UUID): DiaryEntries = diaryEntriesRepository.findById(diaryId).orElseThrow()

}
