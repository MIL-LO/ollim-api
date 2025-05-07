package com.millo.ollim.diary.dto

import com.fasterxml.jackson.annotation.JsonProperty

class RecommendDTO {

    data class DiaryRequestToAI(
        @JsonProperty(value = "user_id")
        val userId: String,
        @JsonProperty(value = "diary_id")
        val diaryId: String,
        val content: String,
        val persona: Persona
    )

    data class Persona(
        val mbti: String,
        @JsonProperty(value = "age_group")
        val ageGroup: String,
        val lifestyle: String,
    )

    data class AIRecommendation(
        @JsonProperty("id")
        val id: String,

        @JsonProperty("name")
        val name: String,

        @JsonProperty("description")
        val description: String,

        @JsonProperty("image_url")
        val imageUrl: String,

        @JsonProperty("image_width")
        val imageWidth: Int,

        @JsonProperty("image_height")
        val imageHeight: Int,

        @JsonProperty("owner_id")
        val ownerId: String,

        @JsonProperty("owner_name")
        val ownerName: String,

        @JsonProperty("followers")
        val followers: Int,

        @JsonProperty("track_summary")
        val trackSummary: String,

        @JsonProperty("tag_emotion")
        val tagEmotion: String,

        @JsonProperty("similarity")
        val similarity: Double
    )
}
