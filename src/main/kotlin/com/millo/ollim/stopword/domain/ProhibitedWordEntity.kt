package com.millo.ollim.stopword.domain

import com.millo.ollim.common.domain.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "prohibited_words")
data class ProhibitedWordEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @Column(name = "word")
    var word: String,
    @Column(name = "is_active")
    var isActive: Boolean,
):BaseTimeEntity() {
    constructor(word: String) : this(
        0,word,true
    )
}
