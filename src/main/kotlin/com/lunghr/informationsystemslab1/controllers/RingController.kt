package com.lunghr.informationsystemslab1.controllers

import com.lunghr.informationsystemslab1.dto.RingRepository
import com.lunghr.informationsystemslab1.models.Ring
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/rings")
class RingController(
    @Autowired private val ringRepository: RingRepository,
) {
    @GetMapping
    fun getAllRings(): List<Ring> {
        val rings = ringRepository.findAll().toList()
        println("rings $rings")
        return rings
    }

    @PostMapping
    fun addRing(
        @RequestBody ring: Ring,
    ): ResponseEntity<Ring> {
        println("add $ring")
        val savedRing = ringRepository.save(ring)
        return ResponseEntity(savedRing, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getRingById(
        @PathVariable id: Int,
    ): ResponseEntity<Ring> {
        val ring = ringRepository.findById(id)
        return if (ring.isPresent) {
            ResponseEntity(ring.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}
