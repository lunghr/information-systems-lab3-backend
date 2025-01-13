package com.lunghr.informationsystemslab1.auth.model.dto

import com.lunghr.informationsystemslab1.auth.model.ent.RequestStatus
import com.lunghr.informationsystemslab1.auth.model.ent.Role
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(name = "Request Response Dto")
class RequestResponseDto(

    @Schema(description = "Request Id", example = "1")
    @NotNull(message = "Request Id is required")
    val id: Long,

    @Schema(description = "User Id", example = "1")
    val userId: Long,

    @Schema(description = "Username", example = "Oleg")
    val username: String,

    @Schema(description = "Status", example = "APPROVED")
    val status: RequestStatus,

    @Schema(description = "Approval Role", example = "ROLE_ADMIN")
    val role: Role
)
