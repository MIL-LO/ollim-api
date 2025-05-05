package com.millo.ollim.stopword.controller

import com.millo.ollim.stopword.config.ProhibitedTrie
import com.millo.ollim.stopword.config.ProhibitedWordCache
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stopword")
@Tag(name = "금칙어 기능", description = "금칙어 목록 조회, 욕설 필터링 두가지 제공")
class ProhibiteController(
    @Autowired private val prohibitedWordCache: ProhibitedWordCache
) {

    @GetMapping("")
    @Operation(summary = "금칙어 전체 조회", description = "모든 금칙어를 불러옵니다.")
    fun test(): ResponseEntity<ProhibitedTrie.Trie> {
        return ResponseEntity.ok().body(prohibitedWordCache.trie)
    }

    // 다이어리 욕설 부분 잘라서 반환
    @GetMapping("/catch")
    @Operation(summary = "발견된 금칙어 출력", description = "요청 문자열에서 발견된 모든 금칙어를 불러옵니다.")
    fun catch(@RequestParam(value = "input", defaultValue = "") input: String): ResponseEntity<List<String>> {
        val sentence = input.replace("[^가-힣a-zA-Z]".toRegex(), "").trim()

        val res = prohibitedWordCache.catch(sentence)
        return ResponseEntity.ok().body(res)
    }

    @GetMapping("/filter")
    @Operation(summary = "금칙어 필터링", description = "요청 문자열에서 발견된 모든 금칙어를 '어머'로 변환합니다..")
    fun filter(@RequestParam(value = "input", defaultValue = "") input: String): ResponseEntity<String> {
        val sentence = input.replace("[^가-힣a-zA-Z]".toRegex(), "").trim()

        val res = prohibitedWordCache.filter(sentence)
        return ResponseEntity.ok().body(res)
    }
}
