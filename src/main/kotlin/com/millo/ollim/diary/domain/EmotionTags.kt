package com.millo.ollim.diary.domain

import com.millo.ollim.common.domain.BaseTimeEntity
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
):BaseTimeEntity()
{

    constructor(request: TagRequest) : this(
        request.id,request.name,request.description,
        request.color,request.category,
        request.group,request.isActive
    )

    constructor(original: EmotionTags, request: TagRequest) : this(
        request.id,request.name,request.description,
        request.color,request.category,
        request.group,request.isActive)

    constructor(id: Int) : this(
        id = id,
        name = "",
        description = "",
        color = "",
        category = "",
        group = "",
        isActive = true
    )

}

