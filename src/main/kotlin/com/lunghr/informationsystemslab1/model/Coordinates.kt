package com.lunghr.informationsystemslab1.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Entity
@Table(name = "coordinates")
class Coordinates(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0L,

    @Column(name = "x", nullable = false)
    @Max(506)
    private val x: Int,

    @Column(name = "y", nullable = false)
    @Min((-376).toLong())
    private val y: Double,
)
