package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryContentEntity
import com.millo.ollim.diary.domain.DiaryEntryEntity
import com.millo.ollim.diary.repository.DiaryContentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DiaryContentsService(@Autowired val diaryContentsRepository: DiaryContentsRepository) {

    @Transactional(readOnly = false)
    fun save(diaryEntriesEntity: DiaryEntryEntity, contents: String, imgUrl: String): DiaryContentEntity {
        val res = diaryContentsRepository.save(DiaryContentEntity(diaryEntriesEntity.id, diaryEntriesEntity.userId, contents, imgUrl))
        return res
    }

    @Transactional(readOnly = true)
    fun findByDiaryId(id: UUID): DiaryContentEntity {
        val res = diaryContentsRepository.findById(id.toString()).orElse(null)
        return res
    }

    @Transactional(readOnly = false)
    fun delete(diaryId: UUID) {
        diaryContentsRepository.deleteById(diaryId.toString())
    }

    @Transactional(readOnly = false)
    fun update(id: UUID, content: String, imgUrl: String): DiaryContentEntity {
        val diaryContent = diaryContentsRepository.findById(id.toString()).orElse(null)
        diaryContentsRepository.deleteById(diaryContent.id)
        diaryContent.update(content, imgUrl)
        diaryContentsRepository.save(diaryContent)
        return diaryContent
    }

}
