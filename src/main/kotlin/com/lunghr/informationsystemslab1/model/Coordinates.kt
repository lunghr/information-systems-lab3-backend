package com.lunghr.informationsystemslab1.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "coordinates")
class Coordinates(
    @Id
    private val id: Long,

    @OneToOne
    @JoinColumn(name = "creature_id")
    private val creature: BookCreature,

    private val x: Int,
    private val y: Double,

)
