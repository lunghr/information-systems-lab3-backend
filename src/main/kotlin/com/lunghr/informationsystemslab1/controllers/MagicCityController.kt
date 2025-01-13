package com.lunghr.informationsystemslab1.controllers

import com.lunghr.informationsystemslab1.dto.MagicCityDto
import com.lunghr.informationsystemslab1.dto.MagicCityResponseDto
import com.lunghr.informationsystemslab1.model.BookCreature
import com.lunghr.informationsystemslab1.service.MagicCityService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/city")
@Tag(name = "Magic Cities")
class MagicCityController @Autowired constructor(
    private val magicCityService: MagicCityService
) {
    @Transactional
    @PostMapping("/create")
    fun createCity(
        @RequestHeader("Authorization") token: String,
        @Valid @RequestBody city: MagicCityDto
    ): MagicCityResponseDto {
        return magicCityService.createMagicCity(city, token)
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    fun deleteCity(@RequestHeader("Authorization") token: String, @PathVariable id: Long) {
        magicCityService.deleteMagicCityById(id, token)
    }

    @GetMapping("/all-creatures/{id}")
    fun getAllCreatures(@PathVariable id: Long): List<BookCreature> {
        return magicCityService.getAllCreaturesInCity(id)
    }

    @Transactional
    @PostMapping("/update-city/{id}")
    fun updateCity(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: Long,
        @Valid @RequestBody city: MagicCityDto
    ): MagicCityResponseDto {
        return magicCityService.createMagicCityResponseDtoObject(magicCityService.updateMagicCity(id, city, token))
    }
}
