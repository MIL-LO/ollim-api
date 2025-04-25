package com.millo.ollim.diary.response

import com.millo.ollim.diary.domain.DiaryContents
import com.millo.ollim.diary.domain.DiaryEmotions
import com.millo.ollim.diary.domain.DiaryEntries

data class DiaryResponse(
    val diaryEntries: DiaryEntries,
    val diaryContents: DiaryContents,
    val diaryEmotions: List<DiaryEmotions>
) {
}
