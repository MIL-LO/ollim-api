package com.millo.ollim.diary.service.impl

import com.millo.ollim.diary.domain.DiaryEntryEntity
import com.millo.ollim.diary.dto.DiaryDTO
import com.millo.ollim.diary.dto.RecommendDTO
import com.millo.ollim.diary.service.*
import com.millo.ollim.user.service.UserProfileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class DiaryServiceImpl(
    @Autowired val diaryEntriesService: DiaryEntriesService,
    @Autowired val diaryContentsService: DiaryContentsService,
    @Autowired val diaryEmotionsService: DiaryEmotionsService,
    @Autowired val diaryCollectionItemsService: DiaryCollectionItemsService,
    @Autowired val userProfileService: UserProfileService
    ): DiaryService  {

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
    override fun createNewDiary(userId: UUID, newCreateRequest: DiaryDTO.CreateRequest): DiaryDTO.DiaryResponse {

        val entry = diaryEntriesService.save(DiaryEntryEntity(userId,newCreateRequest.mood,newCreateRequest.emotionTags.toString()))
        val content = diaryContentsService.save(entry,newCreateRequest.content,newCreateRequest.imgUrl);
        val diaryEmotions = diaryEmotionsService.save(entry.id, newCreateRequest.emotionTags)
        val profile = userProfileService.getProfile(userId)

        val recommend = sendDiaryToAI(RecommendDTO.DiaryRequestToAI(
            userId.toString(),
            entry.id.toString(),
            content.content,
            RecommendDTO.Persona(
            profile.mbti.toString(),
            20.toString(),
            profile.activitySpaces.toString()
        )))
        return DiaryDTO.DiaryResponse(entry,content,diaryEmotions,recommend)
    }

    @Transactional(readOnly = true)
    override fun getDiaries(userId: UUID, pageNum:Int): List<DiaryDTO.DiaryResponse> {
        val res:MutableList<DiaryDTO.DiaryResponse> = mutableListOf()
        if (pageNum<1)
            throw Exception("page num 1보다 작음")

        val pageRequest = PageRequest.of(pageNum-1, 10, Sort.by("createdAt").descending())

        val entries = diaryEntriesService.findByUserIdWithIsNotDeleted(userId, pageRequest)

        entries.forEach { diaryEntry ->
            res.add(getDiaryResponse(diaryEntry))
        }

        return res
    }

    // 다이어리 응답 객체 생성
    override fun getDiaryResponse(diaryEntry: DiaryEntryEntity) = DiaryDTO.DiaryResponse(
        diaryEntry,
        diaryContentsService.findByDiaryId(diaryEntry.id),
        diaryEmotionsService.findByDiaryId(diaryEntry.id)
    )

    @Transactional(readOnly = false)
    override fun updateDiary(userId: UUID, updateDiary: DiaryDTO.UpdateRequest): DiaryDTO.DiaryResponse {
        val diaryEntry:DiaryEntryEntity = diaryEntriesService.findById(updateDiary.diaryId)
        if (diaryEntry.userId != userId) {
            throw Exception("유저 아이디 불일치")
        }
        diaryEntriesService.save(
            DiaryEntryEntity(userId,updateDiary,diaryEntry.isDeleted)
        )

        val diaryContents = diaryContentsService.update(diaryEntry.id, updateDiary.content,updateDiary.imgUrl)

        diaryEmotionsService.deleteByDiaryId(diaryEntry.id)
        val diaryEmotions = diaryEmotionsService.save(diaryEntry.id, updateDiary.emotionTags)

        return DiaryDTO.DiaryResponse(diaryEntry,diaryContents, diaryEmotions)
    }

    @Transactional(readOnly = false)
    override fun deleteDiary(userId: UUID, diaryId: UUID) {
        val diaryEntry = diaryEntriesService.findById(diaryId)
        if (diaryEntry.userId != userId) {
            throw Exception("유저 아이디 불일치")
        }

        diaryEmotionsService.deleteByDiaryId(diaryId)
        diaryCollectionItemsService.delete(diaryId)
        diaryContentsService.delete(diaryId)
        diaryEntriesService.delete(diaryId)

    }

    @Transactional(readOnly = true)
    override fun getDiary(userId: UUID, diaryId: UUID): DiaryDTO.DiaryResponse {
        val diaryEntry = diaryEntriesService.findById(diaryId)

        if (diaryEntry.userId != userId) {
            throw Exception("유저 아이디 불일치")
        }

        return DiaryDTO.DiaryResponse(diaryEntry,
            diaryContentsService.findByDiaryId(diaryId),
            diaryEmotionsService.findByDiaryId(diaryId))
    }

    fun sendDiaryToAI(diaryRequestToAI: RecommendDTO.DiaryRequestToAI): Array<RecommendDTO.AIRecommendation>? {
        val url = "http://localhost:8001/recommend"
        val restTemplate = RestTemplate()
        val response: ResponseEntity<Array<RecommendDTO.AIRecommendation>> =
            restTemplate.postForEntity(url, diaryRequestToAI,  Array<RecommendDTO.AIRecommendation>::class.java)
        println("response = ${response.body}")

        return response.body
    }
}
