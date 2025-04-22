package com.millo.ollim.health.repository

import com.millo.ollim.health.domain.DummyDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface DummyDocumentRepository: MongoRepository<DummyDocument, String>
