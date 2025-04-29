package com.millo.ollim.diary.controller

import com.millo.ollim.diary.service.DiaryService
import com.millo.ollim.diary.domain.DiaryCollectionItems
import com.millo.ollim.diary.domain.DiaryCollections
import com.millo.ollim.diary.domain.EmotionTags
import com.millo.ollim.diary.request.CollectionRequest
import com.millo.ollim.diary.request.DiaryRequest
import com.millo.ollim.diary.request.TagRequest
import com.millo.ollim.diary.request.UpdateDiary
import com.millo.ollim.diary.response.DiaryResponse
import com.millo.ollim.diary.service.DiaryCollectionItemsService
import com.millo.ollim.diary.service.DiaryCollectionsService
import com.millo.ollim.diary.service.EmotionTagsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/diary")
class DiaryController(
    @Autowired val diaryService: DiaryService,
    @Autowired val emotionTagsService: EmotionTagsService,
    @Autowired val diaryCollectionsService: DiaryCollectionsService,
    @Autowired val diaryCollectionsItemsService: DiaryCollectionItemsService
) {

    // 다이어리 CRUD
    // 다이어리 생성, 조회(단일, 목록)수정(수정 시, DiaryEmotions 변경 주의)
    @PostMapping("")
    fun createNewDiary(@RequestParam userId: UUID, @RequestBody newDiaryRequest: DiaryRequest):
        ResponseEntity<DiaryResponse> = ResponseEntity.ok().body(diaryService.createNewDiary(userId,newDiaryRequest))
    @PutMapping("")
    fun updateDiary(@RequestParam userId: UUID, @RequestBody updateDiary: UpdateDiary): ResponseEntity<DiaryResponse> =
        ResponseEntity.ok().body(diaryService.updateDiary(userId,updateDiary))
    @DeleteMapping("")
    fun deleteDiary(@RequestParam userId:UUID, @RequestParam diaryId:UUID): ResponseEntity<String> =
        ResponseEntity.ok().body(diaryService.deleteDiary(userId,diaryId))
    @GetMapping("/list")
    fun getDiaries(@RequestParam userId:UUID): ResponseEntity<List<DiaryResponse>> =
        ResponseEntity.ok().body(diaryService.getDiaries(userId))
    @GetMapping("")
    fun getDiary(@RequestParam userId: UUID, @RequestParam diaryId: UUID): ResponseEntity<DiaryResponse> =
        ResponseEntity.ok().body(diaryService.getDiary(userId,diaryId))

    // 태그 CRUD
    // 태그 생성, 목록 조회(단일은 필요 없음), 수정, 제거(제거 시 DiaryEmotions에도 제거 작업 필요)
    @PostMapping("/tag")
    fun createNewTag(@RequestBody request: TagRequest):ResponseEntity<String> =
        ResponseEntity.ok().body(emotionTagsService.createNewTag(request))
    @GetMapping("/tags")
    fun getTags():ResponseEntity<List<EmotionTags>> =
        ResponseEntity.ok().body(emotionTagsService.getTags())
    @PutMapping("/tag")
    fun updateTag(@RequestBody request: TagRequest):ResponseEntity<String> =
        ResponseEntity.ok().body(emotionTagsService.updateTag(request))
    @DeleteMapping("/tag")
    fun deleteTag(@RequestParam tagId:Int): ResponseEntity<String> =
        ResponseEntity.ok().body(emotionTagsService.delete(tagId))

    // 컬렉션 CRUD
    @PostMapping("/collection")
    fun createCollection(@RequestParam userId: UUID, @RequestBody request: CollectionRequest):ResponseEntity<DiaryCollections> =
        ResponseEntity.ok().body(diaryCollectionsService.createNewCollections(userId,request))
    @GetMapping("/collections")
    fun getUserCollection(@RequestParam userId: UUID):ResponseEntity<List<DiaryCollections> > =
        ResponseEntity.ok().body(diaryCollectionsService.getUserCollection(userId))
    @PostMapping("/collection/item")
    fun addCollectionItem(@RequestParam userId: UUID, @RequestParam collectionId: UUID, @RequestParam diaryId: UUID):ResponseEntity<DiaryCollectionItems> =
        ResponseEntity.ok().body(diaryCollectionsItemsService.addCollectionItem(userId,collectionId, diaryId))
    @GetMapping("/collection/item")
    fun getUserCollectionItems(@RequestParam userId: UUID, @RequestParam collectionId:UUID): ResponseEntity<List<DiaryCollectionItems>> =
        ResponseEntity.ok().body(diaryCollectionsItemsService.getUserCollectionItems(userId, collectionId))


}
