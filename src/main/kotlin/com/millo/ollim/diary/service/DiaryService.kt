package com.millo.ollim.diary.service

import com.millo.ollim.diary.domain.DiaryEntries
import com.millo.ollim.diary.request.DiaryRequest
import com.millo.ollim.diary.request.UpdateDiary
import com.millo.ollim.diary.response.DiaryVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
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
    fun createNewDiary(userId: UUID, newDiaryRequest: DiaryRequest): DiaryVO {

        val entry = diaryEntriesService.save(DiaryEntries(userId,newDiaryRequest.mood,newDiaryRequest.emotionTags.toString()))
        val content = diaryContentsService.save(entry,newDiaryRequest.content,newDiaryRequest.imgUrl);
        val diaryEmotions = diaryEmotionsService.save(entry.id, newDiaryRequest.emotionTags)

        return DiaryVO(entry,content,diaryEmotions)
    }

    @Transactional(readOnly = true)
    fun getDiaries(userId: UUID, pageNum:Int): List<DiaryVO> {
        val res:MutableList<DiaryVO> = mutableListOf()
        if (pageNum<1)
            throw Exception("page num 1보다 작음")

        val pageRequest = PageRequest.of(pageNum-1, 10, Sort.by("createdAt").descending())

        val entries = diaryEntriesService.findByUserIdWithIsNotDeleted(userId, pageRequest)

        entries.forEach { diaryEntry ->
            res.add(DiaryVO(
                diaryEntry,
                diaryContentsService.findByDiaryId(diaryEntry.id),
                diaryEmotionsService.findByDiaryId(diaryEntry.id)))
        }

        return res
    }

    @Transactional(readOnly = false)
    fun updateDiary(userId: UUID, updateDiary: UpdateDiary): DiaryVO {
        val diaryEntry:DiaryEntries = diaryEntriesService.findById(updateDiary.diaryId)
        if (diaryEntry.userId != userId) {
            throw Exception("유저 아이디 불일치")
        }
        diaryEntriesService.save(
            DiaryEntries(userId,updateDiary,diaryEntry.isDeleted)
        )

        val diaryContents = diaryContentsService.update(diaryEntry.id, updateDiary.content,updateDiary.imgUrl)

        if (diaryContents == null) {
            throw IllegalStateException("diaryContents를 찾을 수 없습니다: id=${diaryEntry.id}")
        }

        diaryEmotionsService.deleteByDiaryId(diaryEntry.id)
        val diaryEmotions = diaryEmotionsService.save(diaryEntry.id, updateDiary.emotionTags)
        println("diaryEmotions = ${diaryEmotions}")
        return DiaryVO(diaryEntry,diaryContents, diaryEmotions)
    }

    @Transactional(readOnly = false)
    fun deleteDiary(userId: UUID, diaryId: UUID): String {
        // diary 연결된 놈들
        // diaryEmotions, diaryCollections, diary_entry, diary_contents
        val diaryEntry = diaryEntriesService.findById(diaryId)
        if (diaryEntry.userId != userId) {
            throw Exception("유저 아이디 불일치")
        }

        diaryEmotionsService.deleteByDiaryId(diaryId)
        diaryCollectionItemsService.delete(diaryId)
        diaryContentsService.delete(diaryId)
        diaryEntriesService.delete(diaryId)

        return "success"
    }

    @Transactional(readOnly = true)
    fun getDiary(userId: UUID, diaryId: UUID): DiaryVO {
        val diaryEntry = diaryEntriesService.findById(diaryId)

        if (diaryEntry.userId != userId) {
            throw Exception("유저 아이디 불일치")
        }

        return DiaryVO(diaryEntry,
            diaryContentsService.findByDiaryId(diaryId),
            diaryEmotionsService.findByDiaryId(diaryId))
    }
}
