package com.millo.ollim.health.repository

import com.millo.ollim.health.domain.DummyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(path = "dummy")
interface DummyRepository: JpaRepository<DummyEntity, Long> {
}
