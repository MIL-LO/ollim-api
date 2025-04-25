package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryEntries
import com.millo.ollim.diary.repository.EmotionTagsRepository
import com.millo.ollim.diary.request.NewDiaryRequest
import com.millo.ollim.diary.response.DiaryResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.Collections
import java.util.UUID

@Service
class DiaryService(
    @Autowired val diaryEntriesService: DiaryEntriesService,
    @Autowired val diaryContentsService: DiaryContentsService,
    @Autowired val emotionTagsService: EmotionTagsService,
    @Autowired val diaryEmotionsService: DiaryEmotionsService,
) {
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
    fun createNewDiary(userId: UUID, newDiaryRequest: NewDiaryRequest): DiaryResponse {

        val entry = diaryEntriesService.save(DiaryEntries(userId,newDiaryRequest.mood,newDiaryRequest.emotionTags.toString()))
        val content = diaryContentsService.save(entry,"contents","img_url");
        val diaryEmotions = diaryEmotionsService.save(entry.id, newDiaryRequest.emotionTags)

//        val diaryEmotions =
        return DiaryResponse(entry,content,diaryEmotions)
    }

    @Transactional(readOnly = false)
    fun getDiaries(userId: UUID): List<DiaryResponse> {
        val res:MutableList<DiaryResponse> = mutableListOf()
        val entries = diaryEntriesService.findByUserId(userId)
        entries.forEach { diaryEntry ->
            val diaryResponse =DiaryResponse(diaryEntry, diaryContentsService.findByDiaryId(diaryEntry.id),Collections.emptyList())
            println("diaryResponse = ${diaryResponse}")
            res.add(diaryResponse)
        }
        println("res = ${res}")
        return res
    }
}
