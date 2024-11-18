package com.lunghr.informationsystemslab1.models

class MagicCity(
    private var name: String,
    private var population: Int,
    private var area: Double,
    private val establishedData: java.time.LocalDateTime,
    private var governor: BookCreatureType,
    private var capital: Boolean,
    private var populationDensity: Double
) {
}