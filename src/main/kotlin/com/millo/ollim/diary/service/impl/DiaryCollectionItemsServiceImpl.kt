package com.millo.ollim.diary.service.impl

import com.millo.ollim.diary.domain.DiaryCollectionItemEntity
import com.millo.ollim.diary.dto.CollectionDTO
import com.millo.ollim.diary.repository.DiaryCollectionItemsRepository
import com.millo.ollim.diary.service.DiaryCollectionItemsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DiaryCollectionItemsServiceImpl(
    @Autowired val diaryCollectionItemsRepository: DiaryCollectionItemsRepository
): DiaryCollectionItemsService {

    @Transactional(readOnly = false)
    override fun addCollectionItem(userId: UUID, request: CollectionDTO.AddItemRequest) {
        val items:List<DiaryCollectionItemEntity> = diaryCollectionItemsRepository.findAllByIdDiaryCollectionId(request.collectionId)

        diaryCollectionItemsRepository.save(DiaryCollectionItemEntity(request.diaryId,request.collectionId,items.size))
    }

    @Transactional(readOnly = false)
    override fun delete(diaryId: UUID) {
        diaryCollectionItemsRepository.deleteByIdDiaryId(diaryId)
    }

    @Transactional(readOnly = false)
    override fun changeOrder(collectionId: UUID,diaryId1: UUID, diaryId2: UUID){
        val diary1 = diaryCollectionItemsRepository.findByIdDiaryCollectionIdAndIdDiaryId(collectionId,diaryId1)
        val diary2 = diaryCollectionItemsRepository.findByIdDiaryCollectionIdAndIdDiaryId(collectionId,diaryId2)
        val temp = diary1.sortOrder
        diary1.sortOrder = diary2.sortOrder
        diary2.sortOrder = temp

        diaryCollectionItemsRepository.save(diary1)
        diaryCollectionItemsRepository.save(diary2)

    }
}
