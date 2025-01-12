package com.lunghr.informationsystemslab1.controllers

import com.lunghr.informationsystemslab1.dto.BookCreatureDto
import com.lunghr.informationsystemslab1.dto.BookCreatureResponseDto
import com.lunghr.informationsystemslab1.service.BookCreatureService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book-creatures")
@Tag(name = "Book Creatures")
class BookCreatureController @Autowired constructor(
    private val bookCreatureService: BookCreatureService
) {

    @PostMapping("/create")
    fun createBookCreature(
        @RequestHeader("Authorization") token: String,
        @Valid @RequestBody bookCreature: BookCreatureDto
    ): BookCreatureResponseDto {
        return bookCreatureService.createBookCreature(bookCreature, token)
    }
}
