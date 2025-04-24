package com.millo.ollim.diary.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "diary_tags")
data class DiaryTags(
    @Id
    @Column(name = "id", nullable = false)
    val id: UUID,
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
    val createdAt: Date,
    @Column(name = "updated_at")
    val updatedAt: Date,
)

