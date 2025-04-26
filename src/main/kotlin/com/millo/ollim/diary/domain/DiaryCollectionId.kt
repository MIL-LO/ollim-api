package com.millo.ollim.diary.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.*

@Embeddable
data class DiaryCollectionId(
    @Column(name = "diary_id", nullable = false)
    val diaryId: UUID,
    @Column(name = "diary_collection_id", nullable = false)
    val diaryCollectionId: UUID) : Serializable
{

}
