package com.lunghr.informationsystemslab1.model

import lombok.Getter
import lombok.Setter

@Setter
@Getter
class BookCreature(
    private val id: Int,
    private var name: String,
    private var coordinates: Coordinates,
    private val creationDate: java.time.ZonedDateTime,
    private var age: Int,
    private var creatureType: BookCreatureType,
    private var ring: Ring,
    private var creatureLocation: MagicCity,
    private var attackLevel: Float,
)
