package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryContents
import com.millo.ollim.diary.domain.DiaryEntries
import com.millo.ollim.diary.repository.DiaryContentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DiaryContentsService(@Autowired val diaryContentsRepository: DiaryContentsRepository) {

    @Transactional(readOnly = false)
    fun save(diaryEntries: DiaryEntries, contents: String, imgUrl: String): DiaryContents {
        val res = diaryContentsRepository.save(DiaryContents(diaryEntries.id, diaryEntries.userId, contents, imgUrl))
        println("res = ${res}")
        return res
    }

    @Transactional(readOnly = false)
    fun findByDiaryId(id: UUID): DiaryContents {
        val res = diaryContentsRepository.findById(id.toString()).orElse(null)
        return res
    }
    @Transactional(readOnly = false)
    fun save(diaryEntries: DiaryContents): DiaryContents {
        val res = diaryContentsRepository.save(diaryEntries)
        return res
    }
    @Transactional(readOnly = false)
    fun delete(diaryId: UUID) {
        diaryContentsRepository.deleteById(diaryId.toString())
    }

}
