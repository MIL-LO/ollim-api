package com.millo.ollim.diary.controller

import com.millo.ollim.diary.dto.DiaryDTO
import com.millo.ollim.diary.service.DiaryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
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
    fun createNewDiary(@RequestParam userId: UUID, @RequestBody newCreateRequest: DiaryDTO.CreateRequest)
    : ResponseEntity<DiaryDTO.DiaryResponse> =
        ResponseEntity.ok().body(diaryService.createNewDiary(userId,newCreateRequest))

    @GetMapping("/list")
    @Operation(summary = "사용자 다이어리 목록 조회", description = "사용자가 작성한 모든 다이어리를 조회합니다.")
    fun getDiaries(@RequestParam userId:UUID, @RequestParam pageNum:Int)
    : ResponseEntity<List<DiaryDTO.DiaryResponse>> =
        ResponseEntity.ok().body(diaryService.getDiaries(userId, pageNum))

    @GetMapping("")
    @Operation(summary = "다이어리 상세 조회", description = "한 다이어리 상서 정보 조회합니다.")
    fun getDiary(@RequestParam userId: UUID, @RequestParam diaryId: UUID)
    : ResponseEntity<DiaryDTO.DiaryResponse> =
        ResponseEntity.ok().body(diaryService.getDiary(userId,diaryId))

    @PutMapping("")
    @Operation(summary = "다이어리 수정", description = "다이어리의 내용을 수정합니다.")
    fun updateDiary(@RequestParam userId: UUID, @RequestBody updateDiary: DiaryDTO.UpdateRequest)
    : ResponseEntity<DiaryDTO.DiaryResponse> =
        ResponseEntity.ok().body(diaryService.updateDiary(userId,updateDiary))

    @DeleteMapping("")
    @Operation(summary = "다이어리 제거", description = "사용자의 다이어리를 제거합니다.")
    fun deleteDiary(@RequestParam userId:UUID, @RequestParam diaryId:UUID) {
        ResponseEntity.ok().body(diaryService.deleteDiary(userId, diaryId))
    }

}
