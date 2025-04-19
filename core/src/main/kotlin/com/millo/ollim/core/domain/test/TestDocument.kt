package com.millo.ollim.core.domain.test

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "test_documents")
data class TestDocument(
    val id: String? = null,
    val message: String
)
