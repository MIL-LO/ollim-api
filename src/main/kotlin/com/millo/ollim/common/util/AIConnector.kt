package com.millo.ollim.common.util

import com.millo.ollim.diary.dto.RecommendDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class AIConnector(
//    @Value("\${ai.url}") private val url: String
    ) {

    fun sendDiaryToAI(diaryRequestToAI: RecommendDTO.DiaryRequestToAI): Array<RecommendDTO.AIRecommendation>? {

        val url = "http://localhost:8001/recommend"
        val restTemplate = RestTemplate()
        val response: ResponseEntity<Array<RecommendDTO.AIRecommendation>> =
            restTemplate.postForEntity(url, diaryRequestToAI,  Array<RecommendDTO.AIRecommendation>::class.java)
        println("response = ${response.body}")

        return response.body
    }

}
