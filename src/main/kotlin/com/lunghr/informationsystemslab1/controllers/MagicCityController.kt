package com.lunghr.informationsystemslab1.controllers

import com.lunghr.informationsystemslab1.dto.MagicCityDto
import com.lunghr.informationsystemslab1.dto.MagicCityResponseDto
import com.lunghr.informationsystemslab1.service.MagicCityService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/city")
@Tag(name = "Magic Cities")
class MagicCityController @Autowired constructor(
    private val magicCityService: MagicCityService
) {
    @PostMapping("/create")
    fun createCity(@RequestHeader("Authorization") token: String, @Valid @RequestBody city: MagicCityDto): MagicCityResponseDto {
        return magicCityService.createMagicCity(city, token)
    }
}
