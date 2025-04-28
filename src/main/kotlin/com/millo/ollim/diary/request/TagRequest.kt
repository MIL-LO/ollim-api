package com.millo.ollim.diary.request

import com.millo.ollim.diary.domain.DiaryEntryEmotionId

data class TagRequest(
    val id: Int = 0,
    val name: String,
    val description: String,
    val color: String,
    val category: String,
    val group: String,
    val isActive: Boolean,
) {

}
