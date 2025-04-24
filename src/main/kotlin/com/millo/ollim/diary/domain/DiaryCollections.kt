package com.millo.ollim.diary.domain

import jakarta.persistence.*
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
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Date,
    @Column(name = "updated_at")
    val updatedAt: Date,

)
