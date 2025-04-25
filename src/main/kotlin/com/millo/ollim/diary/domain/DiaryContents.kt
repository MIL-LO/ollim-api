package com.millo.ollim.diary.domain

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
    val content: String,
    @Field(name = "image_url")
    val imageUrl: String,
    @Field(name = "created_at")
    val createdAt: LocalDateTime,
    @Field(name = "updated_at")
    val updatedAt: LocalDateTime,
){
    constructor(id:UUID,userId: UUID,content: String,imageUrl: String) : this(
        id.toString(),userId.toString(),content,imageUrl, LocalDateTime.now(),LocalDateTime.now()
    )
}
