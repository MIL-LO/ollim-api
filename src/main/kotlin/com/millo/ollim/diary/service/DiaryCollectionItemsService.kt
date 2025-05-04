package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryCollectionItems
import com.millo.ollim.diary.dto.CollectionDTO
import com.millo.ollim.diary.repository.DiaryCollectionItemsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DiaryCollectionItemsService(
    @Autowired val diaryCollectionItemsRepository: DiaryCollectionItemsRepository
) {

    @Transactional(readOnly = false)
    fun addCollectionItem(userId: UUID, request: CollectionDTO.AddItemRequest) {
        val items:List<DiaryCollectionItems> = diaryCollectionItemsRepository.findAllByIdDiaryCollectionId(request.collectionId)

        diaryCollectionItemsRepository.save(DiaryCollectionItems(request.diaryId,request.collectionId,items.size))
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
