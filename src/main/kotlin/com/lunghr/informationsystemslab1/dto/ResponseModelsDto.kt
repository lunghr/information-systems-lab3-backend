package com.lunghr.informationsystemslab1.dto

import com.lunghr.informationsystemslab1.model.Coordinates
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ring Response DTO")
class RingResponseDto(
    @Schema(description = "Ring ID", example = "1")
    val id: Long,
    @Schema(description = "Ring name", example = "ring")
    val name: String,
    @Schema(description = "Weight", example = "1")
    val weight: Int,
    @Schema(description = "Owner user ID", example = "2")
    val userId: Long
)

@Schema(description = "Book creature Response DTO")
class BookCreatureResponseDto(
    @Schema(description = "Book creature ID", example = "1")
    var id: Long,
    @Schema(description = "Book creature name", example = "creature")
    val name: String,
    @Schema(description = "Coordinates")
    val coordinates: Coordinates,
    @Schema(description = "Creation date", example = "2021-09-01T00:00:00")
    val creationDate: java.time.ZonedDateTime,
    @Schema(description = "Age", example = "1")
    val age: Int,
    @Schema(description = "Creature type", example = "HOBBIT")
    val creatureType: String,
    @Schema(description = "Magic city")
    val creatureLocation: MagicCityResponseDto,
    @Schema(description = "Ring")
    val ring: RingResponseDto,
    @Schema(description = "Attack level", example = "1.0")
    val attackLevel: Float,
    @Schema(description = "Owner user ID", example = "2")
    val userId: Long
)

@Schema(description = "Magic city Response DTO")
class MagicCityResponseDto(
    @Schema(description = "Magic city ID", example = "1")
    val id: Long,
    @Schema(description = "Magic city name", example = "city")
    val name: String,
    @Schema(description = "Magic city area", example = "1.0")
    val area: Double,
    @Schema(description = "Magic city population", example = "1")
    val population: Int,
    @Schema(description = "Established data", example = "2021-09-01T00:00:00")
    val established: java.time.LocalDateTime,
    @Schema(description = "Governor", example = "HOBBIT")
    val governor: String,
    @Schema(description = "Capital", example = "true")
    val capital: Boolean,
    @Schema(description = "Population density", example = "1.0")
    val populationDensity: Double,
    @Schema(description = "Owner user ID", example = "2")
    val userId: Long
)
