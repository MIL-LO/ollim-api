package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryCollectionId
import com.millo.ollim.diary.domain.DiaryCollectionItems
import com.millo.ollim.diary.repository.DiaryCollectionItemsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class DiaryCollectionItemsService(
    @Autowired val diaryCollectionItemsRepository: DiaryCollectionItemsRepository
) {
    fun addCollectionItem(userId: UUID,collectionId: UUID, diaryId: UUID): DiaryCollectionItems {
        val items:List<DiaryCollectionItems> = diaryCollectionItemsRepository.findAllByIdDiaryCollectionId(collectionId)
        if (items.isEmpty()) {
            return diaryCollectionItemsRepository.save(DiaryCollectionItems(diaryId,collectionId,0))
        }
        return diaryCollectionItemsRepository.save(DiaryCollectionItems(diaryId,collectionId,items.size))
    }

    fun getUserCollectionItems(userId: UUID, collectionId: UUID): List<DiaryCollectionItems> {
        return diaryCollectionItemsRepository.findAllByIdDiaryCollectionId(collectionId)
    }

}
