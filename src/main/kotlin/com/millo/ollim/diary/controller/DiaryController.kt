package com.millo.ollim.diary.controller

import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.diary.dto.DiaryDTO
import com.millo.ollim.diary.service.DiaryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/diary")
@Tag(name = "다이어리 기능")
class DiaryController(
    @Autowired val diaryService: DiaryService,
) {

    @PostMapping("")
    @Operation(summary = "다이어리 생성", description = "새로운 다이어리 생성합니다.")
    fun createNewDiary(@AuthenticationPrincipal user: UserPrincipal, @ModelAttribute newCreateRequest: DiaryDTO.CreateRequest)
    : ResponseEntity<DiaryDTO.DiaryResponse> =
        ResponseEntity.ok().body(diaryService.createNewDiary(user.userId,newCreateRequest))

    @GetMapping("/list")
    @Operation(summary = "사용자 다이어리 목록 조회", description = "사용자가 작성한 모든 다이어리를 조회합니다.")
    fun getDiaries(@AuthenticationPrincipal user: UserPrincipal, @RequestParam pageNum:Int)
    : ResponseEntity<List<DiaryDTO.DiaryResponse>> =
        ResponseEntity.ok().body(diaryService.getDiaries(user.userId, pageNum))

    @GetMapping("")
    @Operation(summary = "다이어리 상세 조회", description = "한 다이어리 상서 정보 조회합니다.")
    fun getDiary(@AuthenticationPrincipal user: UserPrincipal, @RequestParam diaryId: UUID)
    : ResponseEntity<DiaryDTO.DiaryResponse> =
        ResponseEntity.ok().body(diaryService.getDiary(user.userId,diaryId))

    @PutMapping("")
    @Operation(summary = "다이어리 수정", description = "다이어리의 내용을 수정합니다.")
    fun updateDiary(@AuthenticationPrincipal user: UserPrincipal, @ModelAttribute updateDiary: DiaryDTO.UpdateRequest)
    : ResponseEntity<DiaryDTO.DiaryResponse> =
        ResponseEntity.ok().body(diaryService.updateDiary(user.userId,updateDiary))

    @DeleteMapping("")
    @Operation(summary = "다이어리 제거", description = "사용자의 다이어리를 제거합니다.")
    fun deleteDiary(@AuthenticationPrincipal user: UserPrincipal, @RequestParam diaryId:UUID) {
        ResponseEntity.ok().body(diaryService.deleteDiary(user.userId, diaryId))
    }

    @GetMapping("/custom")
    @Operation(summary="다이어리 조회 커스텀", description="다이어리 조회를 필요한 것들만 처리하도록 합니다.")
    fun customGetDiary(
        @AuthenticationPrincipal user:UserPrincipal,    // 요청 유저 정보
        @RequestParam diaryId: UUID ?= null,            // 널널~이면 리스트
        @RequestParam pageNum:Int   ?= 1,               // 없으면 1 초기화
        @RequestParam pageSize:Int  ?= 10,              // 없으면 10 초기화
    ){
        ResponseEntity.ok().body(diaryService.customGetDiary(user.userId,diaryId,pageNum,pageSize))
    }
}
