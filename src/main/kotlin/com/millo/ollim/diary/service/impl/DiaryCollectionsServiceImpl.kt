package com.millo.ollim.diary.service.impl

import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.diary.domain.DiaryCollectionEntity
import com.millo.ollim.diary.dto.CollectionDTO
import com.millo.ollim.diary.dto.DiaryDTO
import com.millo.ollim.diary.repository.DiaryCollectionsRepository
import com.millo.ollim.diary.service.DiaryCollectionsService
import com.millo.ollim.diary.service.DiaryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class DiaryCollectionsServiceImpl(
    @Autowired val diaryCollectionsRepository: DiaryCollectionsRepository,
    @Autowired val diaryService: DiaryService,
): DiaryCollectionsService {

    // 컬렉션 생성
    @Transactional(readOnly = false)
    override fun createNewCollections(user: UserPrincipal, createRequest: CollectionDTO.CreateRequest): DiaryCollectionEntity {
        val usersCollections: List<DiaryCollectionEntity> = diaryCollectionsRepository.findAllByUserId(user.userId)
        if (usersCollections.isEmpty()) {
            return diaryCollectionsRepository.save(DiaryCollectionEntity(createRequest,user.userId,0))
        }
        return diaryCollectionsRepository.save(DiaryCollectionEntity(createRequest,user.userId,usersCollections.size))
    }

    // 컬렉션 정보 수정
    @Transactional(readOnly = false)
    override fun updateCollection(user: UserPrincipal, createRequest: CollectionDTO.UpdateRequest) {

        val collection:DiaryCollectionEntity = diaryCollectionsRepository.findByCollectionId(createRequest.collectionId)

        collection.title=createRequest.title
        collection.description=createRequest.description

        diaryCollectionsRepository.save(collection)
    }

    // 사용자가 가지고 있는 모든 컬렉션 출력
    @Transactional(readOnly = true)
    override fun getUserAllCollection(user: UserPrincipal): List<CollectionDTO.CollectionResponse>? {
        return diaryCollectionsRepository.findAllByUserId(user.userId).map { obj -> CollectionDTO.CollectionResponse(obj) }
    }

    // 각 컬렉션에 대한 상세 정보 조회
    @Transactional(readOnly = true)
    override fun getCollectionDetail(user: UserPrincipal, collectionId: UUID): CollectionDTO.CollectionDetailResponse {
        val diaryCollectionEntity:DiaryCollectionEntity =diaryCollectionsRepository.findByCollectionId(collectionId)
        val diaries = mutableListOf<DiaryDTO.DiaryResponse>()
        diaryCollectionEntity.item.forEach { obj->
            diaries.add(diaryService.getDiaryResponse(obj.diary))
        }

        return CollectionDTO.CollectionDetailResponse(diaryCollectionEntity,diaries)
    }

}
