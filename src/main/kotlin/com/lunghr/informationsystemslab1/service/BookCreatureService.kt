package com.lunghr.informationsystemslab1.service

import com.lunghr.informationsystemslab1.auth.services.JwtService
import com.lunghr.informationsystemslab1.auth.services.UserService
import com.lunghr.informationsystemslab1.dto.BookCreatureDto
import com.lunghr.informationsystemslab1.dto.BookCreatureResponseDto
import com.lunghr.informationsystemslab1.model.BookCreature
import com.lunghr.informationsystemslab1.model.BookCreatureType
import com.lunghr.informationsystemslab1.model.exceptions.BookCreatureAlreadyExistsException
import com.lunghr.informationsystemslab1.model.repos.BookCreatureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookCreatureService @Autowired constructor(
    private val bookCreatureRepository: BookCreatureRepository,
    private val userService: UserService,
    private val jwtService: JwtService,
    private val magicCityService: MagicCityService,
    private val ringService: RingService,
    private val coordinatesService: CoordinatesService
) {

    fun createBookCreatureObjects(bookCreatureDto: BookCreatureDto, token: String): BookCreature {
        return bookCreatureRepository.findByName(bookCreatureDto.name)
            ?.let { throw BookCreatureAlreadyExistsException("BookCreature ${bookCreatureDto.name} already exists") }
            ?: bookCreatureRepository.save(
                BookCreature(
                    name = bookCreatureDto.name,
                    coordinates = coordinatesService.saveCoordinates(bookCreatureDto.coordinates),
                    age = bookCreatureDto.age,
                    creatureType = BookCreatureType.valueOf(bookCreatureDto.creatureType),
                    creatureLocation = magicCityService.addCreatureToCity(bookCreatureDto.creatureLocation, token),
                    ring = ringService.addRingToCreature(bookCreatureDto.ring, token),
                    attackLevel = bookCreatureDto.attackLevel,
                    user = userService.getUserByUsername(jwtService.getUsername(jwtService.extractToken(token)))
                )
            )
    }

    fun createBookCreatureResponseDtoObject(bookCreature: BookCreature): BookCreatureResponseDto {
        return BookCreatureResponseDto(
            id = bookCreature.id,
            name = bookCreature.name,
            coordinates = bookCreature.coordinates,
            age = bookCreature.age,
            creationDate = bookCreature.creationDate,
            creatureType = bookCreature.creatureType.toString(),
            creatureLocation = magicCityService.createMagicCityResponseDtoObject(bookCreature.creatureLocation),
            ring = ringService.createRingResponseDtoObject(bookCreature.ring),
            attackLevel = bookCreature.attackLevel,
            userId = bookCreature.user.getId()
        )
    }

    fun createBookCreature(bookCreatureDto: BookCreatureDto, token: String): BookCreatureResponseDto {
        val bookCreature = createBookCreatureObjects(bookCreatureDto, token)
        return createBookCreatureResponseDtoObject(bookCreature)
    }
}
