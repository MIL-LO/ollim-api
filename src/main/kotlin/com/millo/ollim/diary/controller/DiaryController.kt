package com.millo.ollim.diary.controller

import com.millo.ollim.diary.request.NewDiaryRequest
import com.millo.ollim.diary.response.DiaryResponse
import com.millo.ollim.diary.service.DiaryService
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
class DiaryController(@Autowired val diaryService: DiaryService) {

    @PostMapping("")
    fun createNewDiary(@RequestParam userId: UUID, @RequestBody newDiaryRequest: NewDiaryRequest):
        ResponseEntity<DiaryResponse> = ResponseEntity.ok().body(diaryService.createNewDiary(userId,newDiaryRequest))

    @GetMapping("/list")
    fun getDiaries(@RequestParam userId:UUID): ResponseEntity<List<DiaryResponse>> =
        ResponseEntity.ok().body(diaryService.getDiaries(userId))
}
