package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryEntries
import com.millo.ollim.diary.request.DiaryRequest
import com.millo.ollim.diary.request.UpdateDiary
import com.millo.ollim.diary.response.DiaryResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.Collections
import java.util.UUID

@Service
class DiaryService(
    @Autowired val diaryEntriesService: DiaryEntriesService,
    @Autowired val diaryContentsService: DiaryContentsService,
    @Autowired val emotionTagsService: EmotionTagsService,
    @Autowired val diaryEmotionsService: DiaryEmotionsService,
    @Autowired val diaryCollectionItemsService: DiaryCollectionItemsService,
) {

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
    fun createNewDiary(userId: UUID, newDiaryRequest: DiaryRequest): DiaryResponse {

        val entry = diaryEntriesService.save(DiaryEntries(userId,newDiaryRequest.mood,newDiaryRequest.emotionTags.toString()))
        val content = diaryContentsService.save(entry,"contents","img_url");
        val diaryEmotions = diaryEmotionsService.save(entry.id, newDiaryRequest.emotionTags)

        return DiaryResponse(entry,content,diaryEmotions)
    }

    @Transactional(readOnly = true)
    fun getDiaries(userId: UUID): List<DiaryResponse> {
        val res:MutableList<DiaryResponse> = mutableListOf()
        val entries = diaryEntriesService.findByUserId(userId)
        entries.forEach { diaryEntry ->
            val diaryResponse =DiaryResponse(diaryEntry, diaryContentsService.findByDiaryId(diaryEntry.id),diaryEmotionsService.findByDiaryId(diaryEntry.id))
            println("diaryResponse = ${diaryResponse}")
            res.add(diaryResponse)
        }
        println("res = ${res}")
        return res
    }

    @Transactional(readOnly = false)
    fun updateDiary(userId: UUID, updateDiary: UpdateDiary): DiaryResponse {
        val diaryEntry:DiaryEntries = diaryEntriesService.findById(updateDiary.diaryId)
        if (diaryEntry.userId != userId) {
            throw Exception("유저 아이디 불일치")
        }
        diaryEntriesService.save(
            DiaryEntries(userId,updateDiary,diaryEntry.isDeleted,diaryEntry.createdAt,
                LocalDateTime.now()
            )
        )

        val diaryContents = diaryContentsService.findByDiaryId(diaryEntry.id)
        if (diaryContents == null) {
            throw IllegalStateException("diaryContents를 찾을 수 없습니다: id=${diaryEntry.id}")
        }

        diaryContents.content=updateDiary.content
        diaryContents.updatedAt=LocalDateTime.now()
        diaryContents.imageUrl=updateDiary.imgUrl.toString()

        diaryEmotionsService.deleteByDiaryId(diaryEntry.id)
        val diaryEmotions = diaryEmotionsService.save(diaryEntry.id, updateDiary.emotionTags)
        println("diaryEmotions = ${diaryEmotions}")
        return DiaryResponse(diaryEntry,diaryContents, diaryEmotions)
    }

    @Transactional(readOnly = false)
    fun deleteDiary(userId: UUID, diaryId: UUID): String {
        // diary 연결된 놈들
        // diaryEmotions, diaryCollections, diary_entry, diary_contents
        val diaryEntry = diaryEntriesService.findById(diaryId)
        if (diaryEntry.userId != userId) {
            return "invalid user id: $diaryId"
        }

        diaryEmotionsService.deleteByDiaryId(diaryId)
        diaryCollectionItemsService.delete(diaryId)
        diaryContentsService.delete(diaryId)
        diaryEntriesService.delete(diaryId)

        return "success"
    }
}
