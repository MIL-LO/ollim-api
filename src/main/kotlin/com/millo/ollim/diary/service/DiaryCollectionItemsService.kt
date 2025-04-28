package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryCollectionId
import com.millo.ollim.diary.domain.DiaryCollectionItems
import com.millo.ollim.diary.repository.DiaryCollectionItemsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DiaryCollectionItemsService(
    @Autowired val diaryCollectionItemsRepository: DiaryCollectionItemsRepository
) {
    @Transactional
    fun addCollectionItem(userId: UUID,collectionId: UUID, diaryId: UUID): DiaryCollectionItems {
        val items:List<DiaryCollectionItems> = diaryCollectionItemsRepository.findAllByIdDiaryCollectionId(collectionId)
        if (items.isEmpty()) {
            return diaryCollectionItemsRepository.save(DiaryCollectionItems(diaryId,collectionId,0))
        }
        return diaryCollectionItemsRepository.save(DiaryCollectionItems(diaryId,collectionId,items.size))
    }
    @Transactional(readOnly = true)
    fun getUserCollectionItems(userId: UUID, collectionId: UUID): List<DiaryCollectionItems> {
        return diaryCollectionItemsRepository.findAllByIdDiaryCollectionId(collectionId)
    }

    @Transactional(readOnly = false)
    fun delete(diaryId: UUID) {
        diaryCollectionItemsRepository.deleteByIdDiaryId(diaryId)
    }

}
