package com.millo.ollim.diary.controller

import com.millo.ollim.diary.domain.DiaryCollectionItems
import com.millo.ollim.diary.domain.DiaryCollections
import com.millo.ollim.diary.domain.EmotionTags
import com.millo.ollim.diary.request.CollectionRequest
import com.millo.ollim.diary.request.NewDiaryRequest
import com.millo.ollim.diary.request.TagRequest
import com.millo.ollim.diary.response.DiaryResponse
import com.millo.ollim.diary.service.DiaryCollectionItemsService
import com.millo.ollim.diary.service.DiaryCollectionsService
import com.millo.ollim.diary.service.DiaryService
import com.millo.ollim.diary.service.EmotionTagsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/diary")
class DiaryController(
    @Autowired val diaryService: DiaryService,
    @Autowired val emotionTagsService: EmotionTagsService,
    @Autowired val diaryCollectionsService: DiaryCollectionsService,
    @Autowired val diaryCollectionsItemsService: DiaryCollectionItemsService
) {

    @PostMapping("")
    fun createNewDiary(@RequestParam userId: UUID, @RequestBody newDiaryRequest: NewDiaryRequest):
        ResponseEntity<DiaryResponse> = ResponseEntity.ok().body(diaryService.createNewDiary(userId,newDiaryRequest))

    @GetMapping("/list")
    fun getDiaries(@RequestParam userId:UUID): ResponseEntity<List<DiaryResponse>> =
        ResponseEntity.ok().body(diaryService.getDiaries(userId))

    @PostMapping("/tag")
    fun createNewTag(@RequestBody request: TagRequest):ResponseEntity<String> =
        ResponseEntity.ok().body(emotionTagsService.createNewTag(request))

    @GetMapping("/tags")
    fun getTags():ResponseEntity<List<EmotionTags>> =
        ResponseEntity.ok().body(emotionTagsService.getTags())

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
