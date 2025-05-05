package com.millo.ollim.stopword.config

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class ProhibitedWordCache {
    lateinit var trie: Aho.Trie

    @PostConstruct
    fun init() {
        val words = StopwordMaker().fetchBadWords()
        trie = Aho().add(words)
    }

    fun find(sentence:String):List<String>{
        var loc = trie
        var res = mutableListOf<String>()
        var str = ""

        for (c in sentence){
            if (loc.child[c]==null){
                loc=trie
                str=""
                continue
            }
            loc = loc.child[c]!!
            str+=c
            if (loc.end) {
                res.add(str)
                loc=trie
                str=""
            }
        }
        return res
    }
}
