package com.millo.ollim.diary.domain

import com.millo.ollim.diary.request.TagRequest
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

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
        0,request.name,request.description,
        request.color,request.category,
        request.group,true,
        LocalDateTime.now(), LocalDateTime.now(),
    )
}

