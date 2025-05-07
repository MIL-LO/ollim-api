package com.millo.ollim.diary.controller

import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.common.enums.Versions
import com.millo.ollim.diary.dto.CollectionDTO
import com.millo.ollim.diary.service.DiaryCollectionItemsService
import com.millo.ollim.diary.service.DiaryCollectionsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("${Versions.V1}/collection")
@Tag(name = "다이어리 컬렉션 ", description = "사용자가 컬렉션을 생성하고 각 컬렉션에 다이어리를 저장할 수 있습니다.")
class CollectionController(
    @Autowired val diaryCollectionsService: DiaryCollectionsService,
    @Autowired val diaryCollectionItemsService: DiaryCollectionItemsService
) {

    @PostMapping("")
    @Operation(summary = "컬렉션 생성", description = "새로운 컬렉션을 생성합니다.",)
    fun createCollection(
        @AuthenticationPrincipal user: UserPrincipal,
        @RequestBody createRequest: CollectionDTO.CreateRequest
    ){
        diaryCollectionsService.createNewCollections(user, createRequest)
    }

    @PostMapping("/item")
    @Operation(summary = "컬렉션에 다이어리 추가", description = "사용자의 컬렉션에 다이어리를 추가합니다.",)
    fun addCollectionItem(
        @AuthenticationPrincipal user: UserPrincipal,
        @RequestBody addItemRequest: CollectionDTO.AddItemRequest
    ): ResponseEntity<Unit> {

        return ResponseEntity.ok().body(diaryCollectionItemsService.addCollectionItem(user.userId,addItemRequest))
    }

    @GetMapping("/list")
    @Operation(summary = "컬렉션 전체 조회", description = "사용자의 모든 컬렉션들을 불러옵니다.",)
    fun getUserCollection(
        @AuthenticationPrincipal user: UserPrincipal?,
    ): ResponseEntity<List<CollectionDTO.CollectionResponse>> {
        return ResponseEntity.ok().body(diaryCollectionsService.getUserAllCollection(user!!))
    }

    @GetMapping("/item")
    @Operation(summary = "컬렉션 상세 조회", description = "사용자의 한 컬렉션에 해당하는 모든 다이어리를 불러옵니다.",)
    fun getUserCollectionItems(
        @AuthenticationPrincipal user: UserPrincipal?,
        @RequestParam collectionId: UUID
    ): ResponseEntity<CollectionDTO.CollectionDetailResponse> {
        return ResponseEntity.ok().body(diaryCollectionsService.getCollectionDetail(user!!, collectionId))
    }

    @PutMapping("")
    @Operation(summary = "컬렉션 정보 수정", description = "컬렉션 정보 수정합니다.")
    fun updateCollection(
        @AuthenticationPrincipal user: UserPrincipal?,
        @RequestBody updateRequest: CollectionDTO.UpdateRequest
    ){
        diaryCollectionsService.updateCollection(user!!, updateRequest)
    }

    @PutMapping("/item")
    @Operation(summary = "컬렉션 내부 다이어리 정렬 순서 변경 ", description = "두 다이어리의 정렬 순서를 바꿉니다.",)
    fun changeCollectionItemsOrder(
        @RequestParam collectionId: UUID,
        @RequestParam diaryId1: UUID,
        @RequestParam diaryId2: UUID
    ){
       diaryCollectionItemsService.changeOrder(collectionId, diaryId1, diaryId2)
    }
}
