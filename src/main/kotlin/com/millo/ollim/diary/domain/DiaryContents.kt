package com.millo.ollim.diary.domain

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

@Document(collection = "diary_contents")
data class DiaryContents(
    @Id
    @Field(name = "_id")
    val id: UUID,
    @Field(name = "user_id")
    val userId: UUID,
    @Field(name = "content")
    val content: String,
    @Field(name = "image_url")
    val imageUrl: String,
    @Field(name = "created_at")
    val createdAt: Date,
    @Field(name = "updated_at")
    val updatedAt: Date,
)
