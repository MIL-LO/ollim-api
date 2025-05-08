package com.millo.ollim.diary.controller

import com.millo.ollim.common.enums.Versions
import com.millo.ollim.diary.dto.TagDTO
import com.millo.ollim.diary.service.EmotionTagsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("${Versions.V1}/tag")
@Tag(name = "Tag", description = "감정 태그 생성,수정,삭제,전체조회 기능")
class TagController(
    @Autowired val emotionTagsService: EmotionTagsService
)
{

    @PostMapping
    @Operation(summary = "감정 태그 생성", description = "새로운 감정 태그 생성합니다. 동일한 이름 불가")
    fun createNewTag(
        @RequestBody request: TagDTO.CreateRequest) {
        ResponseEntity.ok().body(emotionTagsService.createNewTag(request))
    }

    @GetMapping
    @Operation(summary = "태그 전체 조회", description = "모든 태그를 불러옵니다.")
    fun getTags(): ResponseEntity<List<TagDTO.Response>> {
        return ResponseEntity.ok().body(emotionTagsService.getAllTags())
    }

    @PutMapping
    @Operation(summary = "태그 수정", description = "태그 하나의 정보를 수정합니다. (태그 번호를 포함한 모든 정보 필요)")
    fun updateTag(
        @RequestBody request: TagDTO.UpdateRequest) {
        ResponseEntity.ok().body(emotionTagsService.updateTag(request))
    }

    @DeleteMapping
    @Operation(summary = "태그 삭제", description = "태그를 제거합니다. (태그 번호 필요)")
    fun deleteTag(
        @RequestParam tagId:Int) {
        ResponseEntity.ok().body(emotionTagsService.delete(tagId))
    }
}
