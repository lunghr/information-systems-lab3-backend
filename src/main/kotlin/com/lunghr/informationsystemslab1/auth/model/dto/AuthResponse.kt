package com.lunghr.informationsystemslab1.auth.model.dto

import com.lunghr.informationsystemslab1.auth.model.ent.Role
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Token response DTO")
class AuthResponse(
    @Schema(
        description = "Access token",
        example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvYmxlZyIsImV4cCI6MTYzNzQwNjQwMH0.7"
    )
    var accessToken: String,
    var role: Role
)
