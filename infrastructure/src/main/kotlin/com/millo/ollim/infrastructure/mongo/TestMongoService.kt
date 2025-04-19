package com.millo.ollim.infrastructure.mongo

import com.millo.ollim.core.domain.test.TestDocument
import com.millo.ollim.core.domain.test.TestDocumentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TestMongoService(
    private val testDocumentRepository: TestDocumentRepository
) {
    @Transactional
    fun saveTestData(): TestDocument {
        val document = TestDocument(message = "hello(Mongo)")
        return testDocumentRepository.save(document)
    }

    @Transactional(readOnly = true)
    fun getAllTestData(): List<TestDocument> {
        return testDocumentRepository.findAll()
    }

    @Transactional
    fun deleteTestData(id: String): TestDocument {
        val document = testDocumentRepository.findById(id)
            .orElseThrow { NoSuchElementException("해당 ID의 문서를 찾을 수 없습니다: $id") }

        testDocumentRepository.deleteById(id)
        return document
    }
}
