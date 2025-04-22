package com.millo.ollim.health.controller

import com.millo.ollim.health.domain.DummyDocument
import com.millo.ollim.health.service.MongoService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/mongo")
class MongoHealthController(
    private val testMongoService: MongoService
) {

    @PostMapping
    fun createTest(): DummyDocument {
        return testMongoService.saveTestData()
    }

    @GetMapping
    fun readTest(): List<DummyDocument> {
        return testMongoService.getAllTestData()
    }

    @DeleteMapping("/{id}")
    fun deleteTest(@PathVariable id: String): DummyDocument {
        return testMongoService.deleteTestData(id)
    }
}
