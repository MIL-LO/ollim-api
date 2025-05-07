package com.millo.ollim.diary.service.impl

import com.millo.ollim.diary.domain.DiaryEntryEntity
import com.millo.ollim.diary.repository.DiaryEntriesRepository
import com.millo.ollim.diary.service.DiaryEntriesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DiaryEntriesServiceImpl (
    @Autowired val diaryEntriesRepository: DiaryEntriesRepository
): DiaryEntriesService {

    @Transactional(readOnly = false)
    override fun save(diaryEntriesEntity: DiaryEntryEntity) = diaryEntriesRepository.save(diaryEntriesEntity)
    @Transactional(readOnly = false)
    override fun update(diaryEntriesEntity: DiaryEntryEntity) = diaryEntriesRepository.save(diaryEntriesEntity)
    @Transactional(readOnly = false)
    override fun delete(id: UUID) = diaryEntriesRepository.deleteById(id)

    @Transactional(readOnly = true)
    override fun findByUserIdWithIsNotDeleted(id: UUID, pageRequest: PageRequest) = diaryEntriesRepository.findAllByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(id,pageRequest)

    @Transactional
    override fun findById(diaryId: UUID): DiaryEntryEntity = diaryEntriesRepository.findById(diaryId).orElseThrow()

}
