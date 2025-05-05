package com.millo.ollim.stopword.controller

import com.millo.ollim.stopword.config.StopwordMaker
import com.millo.ollim.stopword.config.Aho
import com.millo.ollim.stopword.config.ProhibitedWordCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stopword")
class ProhibiteController(
    @Autowired private val prohibitedWordCache: ProhibitedWordCache
) {

    @GetMapping("")
    fun test(): ResponseEntity<Aho.Trie> {
        val res = prohibitedWordCache.trie

        return ResponseEntity.ok().body(res)
    }

    @GetMapping("/test")
    fun filter(@RequestParam(value = "input", defaultValue = "") input: String): ResponseEntity<List<String>> {
        val sentence = input.replace("[^가-힣a-zA-Z]", "")
            .replace("-","")
            .replace("\\s".toRegex(), "")
            .trim()
        println("sentence = ${sentence}")
        val res = prohibitedWordCache.find(sentence)
        return ResponseEntity.ok().body(res)
    }

}
