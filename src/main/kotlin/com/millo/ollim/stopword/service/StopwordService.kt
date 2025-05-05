package com.millo.ollim.stopword.service

import com.millo.ollim.stopword.domain.ProhibitedWords
import com.millo.ollim.stopword.repository.ProhibitedWordsRepository
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CachePut
import org.springframework.stereotype.Service

@Service
class StopwordService (
    @Autowired val prohibitedWordsRepository: ProhibitedWordsRepository
){
    companion object {
        const val FORBIDDEN_WORDS_CACHE_NAME: String = "forbiddenWords"
        const val FORBIDDEN_WORDS_CACHE_KEY = "all"
    }

    // 빠르게 검사하려면 아호코라식으로 저장 필요?
    @PostConstruct
    @CachePut(value = [FORBIDDEN_WORDS_CACHE_NAME], key = "'$FORBIDDEN_WORDS_CACHE_KEY'")
    fun loadWords(): Set<String>{
        val words: MutableList<ProhibitedWords> = prohibitedWordsRepository.findAll()

        return emptySet()
    }

}
