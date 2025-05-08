package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryCollectionEntity
import com.millo.ollim.diary.dto.CollectionDTO
import java.util.*

interface DiaryCollectionsService {

    fun getCollectionDetail(userId: UUID, collectionId: UUID): CollectionDTO.CollectionDetailResponse
    fun getUserAllCollection(userId: UUID): List<CollectionDTO.CollectionResponse>?
    fun updateCollection(userid: UUID, createRequest: CollectionDTO.UpdateRequest)
    fun createNewCollections(userId: UUID, createRequest: CollectionDTO.CreateRequest): DiaryCollectionEntity
}
