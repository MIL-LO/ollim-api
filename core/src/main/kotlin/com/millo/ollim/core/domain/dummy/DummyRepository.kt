package com.millo.ollim.core.domain.dummy

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(path = "dummy")
interface DummyRepository: JpaRepository<DummyEntity, Long> {
}
