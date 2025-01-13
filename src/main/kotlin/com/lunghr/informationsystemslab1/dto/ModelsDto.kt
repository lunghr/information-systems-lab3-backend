package com.lunghr.informationsystemslab1.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@Schema(description = "Book creature DTO")
data class BookCreatureDto(
    @Schema(description = "name", example = "Oleg", required = true)
    @NotBlank(message = "Name is required")
    val name: String,

    @Schema(description = "coordinates DTO", required = true)
    @NotNull(message = "Coordinates is required")
    val coordinates: CoordinatesDto,

    @Schema(description = "creation date", example = "2021-09-01T00:00:00")
    val creationDate: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @Schema(description = "age", example = "1", required = false)
    @Positive(message = "Age must be greater than 0")
    val age: Int,

    @Schema(description = "creature type", example = "HOBBIT", required = true)
    @NotBlank(message = "Creature type is required")
    val creatureType: String,

    @Schema(description = "ring Id", required = true)
    @NotNull(message = "Ring is required")
    val ringId: Long,

    @Schema(description = "magic city ID", required = true)
    @NotNull(message = "Magic city is required")
    val creatureLocationId: Long,

    @Schema(description = "attack level", example = "1.0", minimum = ">0", required = false)
    val attackLevel: Float,
)

@Schema(description = "Book creature type DTO")
data class CoordinatesDto(
    @Schema(description = "x", example = "1", maximum = "506", required = true)
    @NotNull(message = "X is required")
    @Max(506, message = "X must be less than 506")
    val x: Int,

    @Schema(description = "y", example = "1.0", minimum = "-376", required = true)
    @NotNull(message = "Y is required")
    @Min(-376, message = "Y must be greater than -377")
    val y: Double
)

@Schema(description = "Magic city DTO")
data class MagicCityDto(
    @Schema(description = "name", example = "Saint P.", required = true)
    @NotBlank(message = "Name is required")
    val name: String,

    @Schema(description = "area", example = "100.0", minimum = ">0", required = true)
    @Positive(message = "Area must be greater than 0")
    @NotNull(message = "Area is required")
    val area: Double,

    @Schema(description = "population", example = "1000000", required = false)
    @Positive(message = "Population must be greater than 0")
    val population: Int,

    @Schema(description = "established data", example = "2021-09-01T00:00:00")
    val establishedData: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @Schema(description = "governor", example = "HOBBIT", required = false)
    val governor: String,

    @Schema(description = "capital", example = "true", required = false)
    val capital: Boolean,

    @Schema(description = "population density", example = "100.0", required = false, minimum = ">0")
    @Positive(message = "Population density must be greater than 0")
    val populationDensity: Double,
)

@Schema(description = "Ring DTO")
data class RingDto(
    @Schema(description = "name", example = "Ring", required = true)
    @NotBlank(message = "Name is required")
    val name: String,

    @Schema(description = "weight", example = "1", minimum = "0", required = false)
    @Positive(message = "Weight must be greater than 0")
    val weight: Int
)
