package com.lunghr.informationsystemslab1.controllers

import com.lunghr.informationsystemslab1.dto.BookCreatureDto
import com.lunghr.informationsystemslab1.dto.BookCreatureResponseDto
import com.lunghr.informationsystemslab1.service.BookCreatureService
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/book-creatures")
@Tag(name = "Book Creatures")
class BookCreatureController @Autowired constructor(
    private val bookCreatureService: BookCreatureService
) {
    @Transactional
    @PostMapping("/create")
    fun createBookCreature(
        @RequestHeader("Authorization") token: String,
        @Valid @RequestBody bookCreature: BookCreatureDto
    ): BookCreatureResponseDto {
        return bookCreatureService.createBookCreature(bookCreature, token)
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    fun deleteBookCreature(@RequestHeader("Authorization") token: String, @PathVariable id: Long) {
        bookCreatureService.deleteBookCreature(id, token)
    }

    @Transactional
    @PostMapping("/update/{id}")
    fun updateBookCreature(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: Long,
        @Valid @RequestBody bookCreature: BookCreatureDto
    ): BookCreatureResponseDto {
        return bookCreatureService.updateBookCreature(id, bookCreature, token)
    }

    @Transactional
    @DeleteMapping("/destroy-elf-cities")
    fun destroyElfCities(@RequestHeader("Authorization") token: String) {
        bookCreatureService.destroyElfCities(token)
    }

    @Transactional
    @PostMapping("/relocate-hobbits")
    fun relocateHobbits(@RequestHeader("Authorization") token: String) {
        bookCreatureService.relocateCreaturesToMordor(token)
    }

    @Transactional
    @DeleteMapping("/delete-by-ring/{ringId}")
    fun deleteBookCreaturesByRing(@RequestHeader("Authorization") token: String, @PathVariable ringId: Long) {
        bookCreatureService.deleteCreatureByRingId(ringId, token)
    }

    @Transactional
    @GetMapping("/get-oldest")
    fun getOldestBookCreature(): BookCreatureResponseDto {
        return bookCreatureService.getOldestCreature()
    }

    @GetMapping("/by-name-part")
    fun getBookCreaturesByNamePart(@RequestParam name: String): List<BookCreatureResponseDto> {
        return bookCreatureService.getBookCreatureByNamePart(name)
    }

    @GetMapping("/{id}")
    fun getBookCreatureById(@PathVariable id: Long): BookCreatureResponseDto {
        return bookCreatureService.getBookCreatureById(id)
    }

    @GetMapping("/all")
    fun getAllBookCreatures(): List<BookCreatureResponseDto> {
        return bookCreatureService.getAllBookCreatures()
    }
}
