package com.millo.ollim.diary.service

import com.millo.ollim.diary.dto.CollectionDTO
import java.util.*

interface DiaryCollectionItemsService {

    fun addCollectionItem(userId: UUID, request: CollectionDTO.AddItemRequest)
    fun delete(diaryId: UUID)
    fun changeOrder(collectionId: UUID, diaryId1: UUID, diaryId2: UUID)
}
