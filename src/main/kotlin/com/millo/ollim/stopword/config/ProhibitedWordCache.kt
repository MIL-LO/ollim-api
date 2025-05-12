package com.millo.ollim.stopword.config

import com.millo.ollim.stopword.domain.ProhibitedWordEntity
import com.millo.ollim.stopword.repository.ProhibitedWordsRepository
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ProhibitedWordCache(
    @Autowired private val prohibitedWordsRepository: ProhibitedWordsRepository
) {

    lateinit var trie: ProhibitedTrie.Trie

    @PostConstruct
    @Transactional(readOnly = false)
    fun init() {
        val prohibitedWords = prohibitedWordsRepository.findAll()
        val words:List<String>

        if(prohibitedWords.size==0) {
            words = ProhibitedWordCreator().fetchBadWords()
            words.forEach { word ->
                prohibitedWordsRepository.save(ProhibitedWordEntity(word))
            }
        }
        else
            words = prohibitedWords.map { it.word }

        trie = ProhibitedTrie().add(words)
    }

    fun catch(sentence:String):List<String>{
        var loc = trie
        val res = mutableListOf<String>()
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
    fun filter(sentence:String):String{
        var loc = trie
        var res = ""
        var str = ""

        for (c in sentence){
            if (loc.child[c]==null){
                loc=trie
                res+=str+c
                str=""
                continue
            }
            loc = loc.child[c]!!
            str+=c
            if (loc.end) {
                loc=trie
                str=""
                str+="어머"
            }
        }
        return res
    }
}
