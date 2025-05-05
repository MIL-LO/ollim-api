package com.millo.ollim.stopword.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.client.RestTemplate

class ProhibitedWordCreator {

    fun fetchBadWords(): List<String> {
        val url = "https://cdn.jsdelivr.net/gh/hlog2e/bad_word_list@master/word_list.json"
        val restTemplate = RestTemplate()
        val response = restTemplate.getForObject(url, String::class.java)

        val mapper = ObjectMapper()
        val rootNode = mapper.readTree(response)
        val wordsNode = rootNode["words"]

        return wordsNode.map { it.asText() }
    }
}
