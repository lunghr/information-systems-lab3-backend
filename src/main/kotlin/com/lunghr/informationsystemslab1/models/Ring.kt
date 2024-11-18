package com.lunghr.informationsystemslab1.models

import jakarta.persistence.*

@Entity
@Table(name = "rings")
data class Ring(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,
    var name: String,
    var weight: Int
)