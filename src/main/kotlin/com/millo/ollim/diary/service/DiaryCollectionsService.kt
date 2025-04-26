package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryCollections
import com.millo.ollim.diary.repository.DiaryCollectionsRepository
import com.millo.ollim.diary.request.CollectionRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class DiaryCollectionsService(
    @Autowired val diaryCollectionsRepository: DiaryCollectionsRepository
) {

    fun createNewCollections(userId: UUID, request: CollectionRequest): DiaryCollections {
        val usersCollections: List<DiaryCollections> = diaryCollectionsRepository.findAllByUserId(userId)
        if (usersCollections.isEmpty()) {
            return diaryCollectionsRepository.save(DiaryCollections(request,userId,0))
        }
        return diaryCollectionsRepository.save(DiaryCollections(request,userId,usersCollections.size))
    }

    fun getUserCollection(userId: UUID): List<DiaryCollections>? {
        return diaryCollectionsRepository.findAllByUserId(userId)
    }

}
