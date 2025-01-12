package com.lunghr.informationsystemslab1.dto

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
    @Schema(description = "plump", example = "1")
    val message: String
)
