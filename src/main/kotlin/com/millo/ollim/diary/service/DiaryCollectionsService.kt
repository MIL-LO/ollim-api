package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryCollections
import com.millo.ollim.diary.repository.DiaryCollectionsRepository
import com.millo.ollim.diary.dto.CollectionDTO
import com.millo.ollim.diary.response.DiaryVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DiaryCollectionsService(
    @Autowired val diaryCollectionsRepository: DiaryCollectionsRepository,
    @Autowired val diaryService: DiaryService,
) {
    // 컬렉션 생성
    @Transactional(readOnly = false)
    fun createNewCollections(userId: UUID, createRequest: CollectionDTO.CreateRequest): DiaryCollections {
        val usersCollections: List<DiaryCollections> = diaryCollectionsRepository.findAllByUserId(userId)
        if (usersCollections.isEmpty()) {
            return diaryCollectionsRepository.save(DiaryCollections(createRequest,userId,0))
        }
        return diaryCollectionsRepository.save(DiaryCollections(createRequest,userId,usersCollections.size))
    }

    // 컬렉션 정보 수정
    @Transactional(readOnly = false)
    fun updateCollection(userid: UUID, createRequest: CollectionDTO.UpdateRequest) {

        val collection:DiaryCollections = diaryCollectionsRepository.findByCollectionId(createRequest.collectionId)

        collection.title=createRequest.title
        collection.description=createRequest.description

        diaryCollectionsRepository.save(collection)
    }

    // 사용자가 가지고 있는 모든 컬렉션 출력
    @Transactional(readOnly = true)
    fun getUserAllCollection(userId: UUID): List<CollectionDTO.CollectionResponse>? {
        return diaryCollectionsRepository.findAllByUserId(userId).map { obj -> CollectionDTO.CollectionResponse(obj) }
    }

    // 각 컬렉션에 대한 상세 정보 조회
    @Transactional(readOnly = true)
    fun getCollectionDetail(userId: UUID, collectionId: UUID): CollectionDTO.CollectionDetailResponse {
        val diaryCollections:DiaryCollections =diaryCollectionsRepository.findByCollectionId(collectionId)
        val diaries = mutableListOf<DiaryVO>()
        diaryCollections.item.forEach { obj->
            diaries.add(diaryService.getDiaryResponse(obj.diary))
        }

        return CollectionDTO.CollectionDetailResponse(diaryCollections,diaries)
    }

}
