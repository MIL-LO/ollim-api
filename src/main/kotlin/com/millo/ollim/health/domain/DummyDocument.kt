package com.millo.ollim.health.domain

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "test_documents")
data class DummyDocument(
    val id: String? = null,
    val message: String
)
