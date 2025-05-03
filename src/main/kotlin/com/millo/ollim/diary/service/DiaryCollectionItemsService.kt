package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryCollectionId
import com.millo.ollim.diary.domain.DiaryCollectionItems
import com.millo.ollim.diary.repository.DiaryCollectionItemsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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
    fun getUserCollectionItems(userId: UUID, collectionId: UUID, pageNum:Int): List<DiaryCollectionItems> {
        val pageRequest = PageRequest.of(pageNum, 10, Sort.by("sortOrder").ascending())

        return diaryCollectionItemsRepository.findAllByIdDiaryCollectionId(collectionId,pageRequest)
    }

    @Transactional(readOnly = false)
    fun delete(diaryId: UUID) {
        diaryCollectionItemsRepository.deleteByIdDiaryId(diaryId)
    }

    @Transactional(readOnly = false)
    fun changeOrder(collectionId: UUID,diaryId1: UUID, diaryId2: UUID){
        val diary1 = diaryCollectionItemsRepository.findByIdDiaryCollectionIdAndIdDiaryId(collectionId,diaryId1)
        val diary2 = diaryCollectionItemsRepository.findByIdDiaryCollectionIdAndIdDiaryId(collectionId,diaryId2)
        val temp = diary1.sortOrder
        diary1.sortOrder = diary2.sortOrder
        diary2.sortOrder = temp

        diaryCollectionItemsRepository.save(diary1)
        diaryCollectionItemsRepository.save(diary2)

    }
}
