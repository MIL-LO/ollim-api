package com.millo.ollim.app.controller

import com.millo.ollim.core.domain.test.TestDocument
import com.millo.ollim.infrastructure.mongo.TestMongoService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mongo")
class TestMongoController(
    private val testMongoService: TestMongoService
) {

    @PostMapping
    fun createTest(): TestDocument {
        return testMongoService.saveTestData()
    }

    @GetMapping
    fun readTest(): List<TestDocument> {
        return testMongoService.getAllTestData()
    }

    @DeleteMapping("/{id}")
    fun deleteTest(@PathVariable id: String): TestDocument {
        return testMongoService.deleteTestData(id)
    }
}
