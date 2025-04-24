package com.millo.ollim.diary.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "diary_collection_items")
data class DiaryCollectionItems(
    @Id
    @Column(name = "diary_collection_id", nullable = false)
    val diaryCollectionId: UUID,
    @Column(name = "diary_id", nullable = false)
    val diaryId: UUID,
    @Column(name = "sort_order")
    val sortOrder: Int,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Date,
)
