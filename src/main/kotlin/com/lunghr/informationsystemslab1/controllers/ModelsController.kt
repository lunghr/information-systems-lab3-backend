package com.lunghr.informationsystemslab1.controllers

import com.lunghr.informationsystemslab1.dto.RingDto
import com.lunghr.informationsystemslab1.dto.RingResponseDto
import com.lunghr.informationsystemslab1.service.ModelsService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ring")
class ModelsController @Autowired constructor(
    private val modelsService: ModelsService
) {
    @PostMapping
    fun createRing(@RequestHeader("Authorization") token: String, @Valid @RequestBody ring: RingDto): RingResponseDto {
        return modelsService.saveRing(ring, token)
    }
}
