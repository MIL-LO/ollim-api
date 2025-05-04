package com.millo.ollim.diary.controller

import com.millo.ollim.diary.service.DiaryService
import com.millo.ollim.diary.dto.DiaryRequest
import com.millo.ollim.diary.dto.UpdateDiary
import com.millo.ollim.diary.response.DiaryVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/diary")
class DiaryController(
    @Autowired val diaryService: DiaryService,
) {

    // 다이어리 CRUD
    // 다이어리 생성, 조회(단일, 목록)수정(수정 시, DiaryEmotions 변경 주의)
    @PostMapping("")
    fun createNewDiary(@RequestParam userId: UUID, @RequestBody newDiaryRequest: DiaryRequest):
        ResponseEntity<DiaryVO> = ResponseEntity.ok().body(diaryService.createNewDiary(userId,newDiaryRequest))
    @PutMapping("")
    fun updateDiary(@RequestParam userId: UUID, @RequestBody updateDiary: UpdateDiary): ResponseEntity<DiaryVO> =
        ResponseEntity.ok().body(diaryService.updateDiary(userId,updateDiary))
    @DeleteMapping("")
    fun deleteDiary(@RequestParam userId:UUID, @RequestParam diaryId:UUID): ResponseEntity<String> =
        ResponseEntity.ok().body(diaryService.deleteDiary(userId,diaryId))
    @GetMapping("/list")
    fun getDiaries(@RequestParam userId:UUID, @RequestParam pageNum:Int): ResponseEntity<List<DiaryVO>> =
        ResponseEntity.ok().body(diaryService.getDiaries(userId, pageNum))
    @GetMapping("")
    fun getDiary(@RequestParam userId: UUID, @RequestParam diaryId: UUID): ResponseEntity<DiaryVO> =
        ResponseEntity.ok().body(diaryService.getDiary(userId,diaryId))

}
