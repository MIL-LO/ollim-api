package com.millo.ollim.diary.domain

import com.millo.ollim.common.domain.BaseTimeEntity
import com.millo.ollim.diary.request.CollectionRequest
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "diary_collections")
data class DiaryCollections(
    @Id
    @Column(name = "id", nullable = false,updatable = false)
    val id: UUID,
    @Column(name = "user_id", nullable = false, updatable = false)
    val userId: UUID,
    @Column(name = "title")
    val title: String,
    @Column(name = "description")
    val description: String,
    @Column(name = "sort_order")
    val sortOrder: Int,
    ): BaseTimeEntity()
{
    constructor(request: CollectionRequest, userId: UUID, sortOrder: Int) : this(
        UUID.randomUUID(), userId, request.title, request.description,
        sortOrder
    )
}
