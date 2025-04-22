package com.millo.ollim.health.domain

import jakarta.persistence.*

@Entity
@Table(name = "dummy")
class DummyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String
)
