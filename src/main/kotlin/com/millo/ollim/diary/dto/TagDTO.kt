package com.millo.ollim.diary.dto

import com.millo.ollim.diary.domain.EmotionTags
import io.swagger.v3.oas.annotations.media.Schema

class TagDTO{

    @Schema(description = "태그 생성 할 때 사용합니다.")
    data class CreateRequest(
        @Schema(description = "태그명(depth2)", example = "꿀꿀")
        val name: String,
        @Schema(description = "태그설명", example = "기분이 꿀꿀할 때를 의미합니다.")
        val description: String,
        @Schema(description = "태그색상", example = "255,255,255,0")
        val color: String,
        @Schema(description = "카테고리", example = "카테고리번호")
        val category: String,
        @Schema(description = "그룹(depth1)", example = "애매모호")
        val group: String,
    )

    @Schema(description = "태그 정보를 수정할 때 사용합니다. ")
    data class UpdateRequest(
        @Schema(description = "태그번호", example = "1")
        val id: Int,
        @Schema(description = "태그명(depth2)", example = "꿀꿀")
        val name: String,
        @Schema(description = "태그설명", example = "기분이 꿀꿀할 때를 의미합니다.")
        val description: String,
        @Schema(description = "태그색상", example = "255,255,255,0")
        val color: String,
        @Schema(description = "카테고리", example = "카테고리번호")
        val category: String,
        @Schema(description = "그룹(depth1)", example = "애매모호")
        val group: String,
        @Schema(description = "활성상태", example = "true")
        val isActive: Boolean,
    )

    data class Response(
        val id: Int,
        val name: String,
        val description: String,
        val color: String,
        val category: String,
        val group: String,
    ) {
        constructor(tag: EmotionTags) : this(
            tag.id,tag.name,tag.description,tag.color,tag.category,tag.group
        )
    }
}
