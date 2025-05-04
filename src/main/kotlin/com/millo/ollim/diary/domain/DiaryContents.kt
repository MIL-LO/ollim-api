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
    var userId: String,
    @Field(name = "content")
    var content: String,
    @Field(name = "image_url")
    var imageUrl: String,
    @Field(name = "created_at")
    var createdAt: LocalDateTime,
    @Field(name = "updated_at")
    var updatedAt: LocalDateTime,
) {
    fun update(content: String, imgUrl: String) {
        this.content=content
        this.imageUrl = imgUrl
        this.updatedAt = LocalDateTime.now()
    }

    constructor(id: UUID, userId: UUID, content: String, imageUrl: String) : this(
        id.toString(), userId.toString(), content, imageUrl, LocalDateTime.now(), LocalDateTime.now()
    )
}
