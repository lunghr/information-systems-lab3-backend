package com.lunghr.informationsystemslab1.auth.model.ent

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Builder
import lombok.Data


@Data
@Schema(description = "Token response DTO")
class TokenResponse(
    @Schema(
        description = "Access token",
        example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvYmxlZyIsImV4cCI6MTYzNzQwNjQwMH0.7"
    )
    var accessToken: String
)