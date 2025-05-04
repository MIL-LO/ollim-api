package com.millo.ollim.diary.dto

import com.millo.ollim.diary.domain.DiaryCollections
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

class CollectionDTO {

    @Schema(description = "컬랙션 생성 요청")
    data class CreateRequest(
        @Schema(description = "제목",example = "추억여행")
        val title: String,
        @Schema(description = "컬렉션 상세 설명",example = "나의 추억 여행 관련 다이어리들")
        val description: String,
    )

    @Schema(description = "컬렉션에 다이어리 추가하는 요청")
    data class AddItemRequest(
        @Schema(description = "컬렉션아이디")
        val collectionId: UUID,
        @Schema(description = "다이어리아이디")
        val diaryId: UUID,
    )

    @Schema(description = "다이어리 수정 처리")
    data class UpdateRequest(
        @Schema(description = "컬렉션아이디")
        val collectionId: UUID,
        @Schema(description = "제목",example = "수정 제목")
        val title: String,
        @Schema(description = "컬렉션 상세 설명",example = "수정 내용")
        val description: String,
    )

    data class CollectionResponse(
        val collectionId: UUID,
        val title: String,
        val description: String,
        val sortOrder: Int,
        val itemCount: Int
    ) {
        constructor(collection: DiaryCollections) : this(
            collection.collectionId,
            collection.title,
            collection.description,
            collection.sortOrder,
            collection.item.size
        )
    }

    data class CollectionDetailResponse(
        val collectionId: UUID,
        val title: String,
        val description: String,
        val sortOrder: Int,
        val item:List<DiaryDTO.DiaryResponse>
    ) {
        constructor(collection: DiaryCollections, diary: MutableList<DiaryDTO.DiaryResponse>) : this(
            collection.collectionId, collection.title, collection.description,
            collection.sortOrder, diary
        )
    }


}
