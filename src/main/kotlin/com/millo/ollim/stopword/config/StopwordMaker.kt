package com.millo.ollim.stopword.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.millo.ollim.stopword.domain.ProhibitedWords
import org.springframework.web.client.RestTemplate

class StopwordMaker {

    val isCreated = false

    fun getStopWordExamples():List<ProhibitedWords>{
        if(isCreated){
            return emptyList()
        }
        val file = javaClass.classLoader.getResourceAsStream("./fword_list.txt")
        val words = file!!.bufferedReader().readText()
        val list = words.split("\n").map { ProhibitedWords(it.trim()) }
        println("list = $list")
        return list
    }

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
