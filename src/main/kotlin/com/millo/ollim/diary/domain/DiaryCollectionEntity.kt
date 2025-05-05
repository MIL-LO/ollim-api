package com.millo.ollim.diary.domain

import com.millo.ollim.common.domain.BaseTimeEntity
import com.millo.ollim.diary.dto.CollectionDTO
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "diary_collections")
data class DiaryCollectionEntity(
    @Id
    @Column(name = "id", nullable = false,updatable = false)
    val collectionId: UUID,
    @Column(name = "user_id", nullable = false, updatable = false)
    val userId: UUID,

    @Column(name = "title")
    var title: String,
    @Column(name = "description")
    var description: String,
    @Column(name = "sort_order")
    var sortOrder: Int,
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_collection_id")
    var item: List<DiaryCollectionItemEntity>

    ): BaseTimeEntity()

{
    constructor(createRequest: CollectionDTO.CreateRequest, userId: UUID, sortOrder: Int) : this(
        UUID.randomUUID(), userId, createRequest.title, createRequest.description,
        sortOrder, emptyList()
    )
}
