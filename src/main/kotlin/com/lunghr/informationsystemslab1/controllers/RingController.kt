package com.lunghr.informationsystemslab1.controllers

import com.lunghr.informationsystemslab1.dto.RingDto
import com.lunghr.informationsystemslab1.dto.RingResponseDto
import com.lunghr.informationsystemslab1.service.RingService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/ring")
@Tag(name = "Rings")
class RingController @Autowired constructor(
    private val ringService: RingService
) {
    @Transactional
    @PostMapping("/create")
    fun createRing(@RequestHeader("Authorization") token: String, @Valid @RequestBody ring: RingDto): RingResponseDto {
        return ringService.createRing(ring, token)
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    fun deleteRing(@RequestHeader("Authorization") token: String, @PathVariable id: Long) {
        ringService.deleteRingById(id, token)
    }
}
