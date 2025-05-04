package com.millo.ollim.diary.domain

import com.millo.ollim.common.domain.BaseTimeEntity
import com.millo.ollim.diary.dto.TagDTO
import jakarta.persistence.*

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

    constructor(request: TagDTO.UpdateRequest) : this(
        request.id,request.name,request.description,
        request.color,request.category,
        request.group,request.isActive
    )
    constructor(request: TagDTO.CreateRequest) : this(
        0,request.name,request.description,
        request.color,request.category,
        request.group,true
    )

    constructor(tagId: Int) : this(
        tagId,"","","","","",false
    )

}

