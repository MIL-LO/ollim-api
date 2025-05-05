package com.millo.ollim.stopword.repository

import com.millo.ollim.stopword.domain.ProhibitedWordEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProhibitedWordsRepository : JpaRepository<ProhibitedWordEntity,Long> {
}
