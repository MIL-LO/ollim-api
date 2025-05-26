package com.millo.ollim.diary.service

import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.diary.domain.DiaryCollectionEntity
import com.millo.ollim.diary.dto.CollectionDTO
import java.util.*

interface DiaryCollectionsService {

    fun getCollectionDetail(user: UserPrincipal, collectionId: UUID): CollectionDTO.CollectionDetailResponse
    fun getUserAllCollection(user: UserPrincipal): List<CollectionDTO.CollectionResponse>?
    fun updateCollection(user: UserPrincipal, createRequest: CollectionDTO.UpdateRequest)
    fun createNewCollections(user: UserPrincipal, createRequest: CollectionDTO.CreateRequest): DiaryCollectionEntity
}
