package com.millo.ollim.diary.domain

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "diary_collection_items")
data class DiaryCollectionItems(
    @EmbeddedId
    val id: DiaryCollectionId,

    @MapsId("diaryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    val diary: DiaryEntries,

    @Column(name = "sort_order")
    var sortOrder: Int,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,
) {
    constructor(diaryId:UUID, collectionId:UUID,sortOrder: Int) : this(
        DiaryCollectionId(diaryId,collectionId),
        DiaryEntries(diaryId),
        sortOrder,
        LocalDateTime.now()
    )
}
