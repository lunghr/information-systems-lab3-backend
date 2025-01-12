package com.lunghr.informationsystemslab1.controllers

import com.lunghr.informationsystemslab1.dto.RingDto
import com.lunghr.informationsystemslab1.dto.RingResponseDto
import com.lunghr.informationsystemslab1.service.RingService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ring")
@Tag(name = "Rings")
class RingController @Autowired constructor(
    private val ringService: RingService
) {

    @PostMapping("/create")
    fun createRing(@RequestHeader("Authorization") token: String, @Valid @RequestBody ring: RingDto): RingResponseDto {
        return ringService.createRing(ring, token)
    }
}
