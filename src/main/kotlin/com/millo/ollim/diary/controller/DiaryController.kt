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

}
