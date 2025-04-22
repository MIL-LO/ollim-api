package com.millo.ollim.health.service

import com.millo.ollim.health.domain.DummyDocument
import com.millo.ollim.health.repository.DummyDocumentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MongoService(
    private val testDocumentRepository: DummyDocumentRepository
) {
    @Transactional
    fun saveTestData(): DummyDocument {
        val document = DummyDocument(message = "hello(Mongo)")
        return testDocumentRepository.save(document)
    }

    @Transactional(readOnly = true)
    fun getAllTestData(): List<DummyDocument> {
        return testDocumentRepository.findAll()
    }

    @Transactional
    fun deleteTestData(id: String): DummyDocument {
        val document = testDocumentRepository.findById(id)
            .orElseThrow { NoSuchElementException("해당 ID의 문서를 찾을 수 없습니다: $id") }

        testDocumentRepository.deleteById(id)
        return document
    }
}
