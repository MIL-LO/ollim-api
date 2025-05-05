package com.millo.ollim.stopword.repository

import com.millo.ollim.stopword.domain.ProhibitedWords
import org.springframework.data.jpa.repository.JpaRepository

interface ProhibitedWordsRepository : JpaRepository<ProhibitedWords,Long> {
}
