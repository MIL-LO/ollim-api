package com.millo.ollim.diary.domain

import com.millo.ollim.diary.request.TagRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "emotion_tags")
data class EmotionTags(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @Column(name = "name")
    val name: String,
    @Column(name = "description")
    val description: String,
    @Column(name = "color")
    val color: String,
    @Column(name = "category")
    val category: String,
    @Column(name = "[group]")
    val group: String,
    @Column(name = "is_active")
    val isActive: Boolean,
    @Column(name = "created_at")
    val createdAt: LocalDateTime,
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime,
){

    constructor(request: TagRequest) : this(
        request.id,request.name,request.description,
        request.color,request.category,
        request.group,request.isActive,
        LocalDateTime.now(), LocalDateTime.now(),
    )

    constructor(original: EmotionTags, request: TagRequest) : this(
        request.id,request.name,request.description,
        request.color,request.category,
        request.group,request.isActive,
        original.createdAt, LocalDateTime.now(),
    )

    constructor(id: Int) : this(
        id = id,
        name = "",
        description = "",
        color = "",
        category = "",
        group = "",
        isActive = true,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

}

