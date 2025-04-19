package com.millo.ollim.core.domain.test

import org.springframework.data.mongodb.repository.MongoRepository

interface TestDocumentRepository: MongoRepository<TestDocument, String>
