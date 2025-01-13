package com.lunghr.informationsystemslab1.auth.model.dto

import com.lunghr.informationsystemslab1.auth.model.ent.Role
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "User Response DTO")
class UserResponseDto(
    @Schema(description = "User Id", example = "1")
    val id: Long,

    @Schema(description = "Username", example = "Oleg")
    val username: String,

    @Schema(description = "Approval Role", example = "ROLE_ADMIN")
    val role: Role
)
