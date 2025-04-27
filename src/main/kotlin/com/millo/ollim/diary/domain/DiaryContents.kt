package com.millo.ollim.diary.domain

import com.millo.ollim.diary.request.UpdateDiary
import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime
import java.util.*

@Document(collection = "diary_contents")
data class DiaryContents(
    @Id
    @Field(name = "_id")
    val id: String,
    @Field(name = "user_id")
    val userId: String,
    @Field(name = "content")
    var content: String,
    @Field(name = "image_url")
    var imageUrl: String,
    @Field(name = "created_at")
    val createdAt: LocalDateTime,
    @Field(name = "updated_at")
    var updatedAt: LocalDateTime,
) {
    constructor(id: UUID, userId: UUID, content: String, imageUrl: String) : this(
        id.toString(), userId.toString(), content, imageUrl, LocalDateTime.now(), LocalDateTime.now()
    )

     constructor(userId: UUID, updateDiary: UpdateDiary, createdAt: LocalDateTime):  this(
        updateDiary.diaryId.toString(),userId.toString(),updateDiary.content,
         updateDiary.imgUrl.toString(),
         createdAt, LocalDateTime.now()
    )

}
