package com.lunghr.informationsystemslab1.auth.controllers

import com.lunghr.informationsystemslab1.auth.services.RequestService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@Service
@RestController
@RequestMapping("/request")
@Tag(name = "Request")
class RequestController @Autowired constructor(
    private val requestService: RequestService
) {
    @Operation(summary = "Request for admin role")
    @PostMapping
    fun requestAdmin(@RequestHeader("Authorization") token: String) {
        requestService.requestAdmin(token)
    }
    @Operation(summary = "Get request id")
    @GetMapping("/request-id/{id}")
    fun getRequestId(@PathVariable id: Long): Long {
        return requestService.getRequestId(id)
    }

}
