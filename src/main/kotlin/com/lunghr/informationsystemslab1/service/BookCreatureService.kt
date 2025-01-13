package com.lunghr.informationsystemslab1.service

import com.lunghr.informationsystemslab1.auth.model.ent.Role
import com.lunghr.informationsystemslab1.auth.services.JwtService
import com.lunghr.informationsystemslab1.auth.services.UserService
import com.lunghr.informationsystemslab1.dto.BookCreatureDto
import com.lunghr.informationsystemslab1.dto.BookCreatureResponseDto
import com.lunghr.informationsystemslab1.model.BookCreature
import com.lunghr.informationsystemslab1.model.BookCreatureType
import com.lunghr.informationsystemslab1.model.exceptions.AccessDeniedException
import com.lunghr.informationsystemslab1.model.exceptions.BookCreatureAlreadyExistsException
import com.lunghr.informationsystemslab1.model.exceptions.BookCreatureNotFoundException
import com.lunghr.informationsystemslab1.model.exceptions.RingAlreadyOwnedException
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

    fun deleteBookCreature(id: Long, token: String) {
        val bookCreature = bookCreatureRepository.findBookCreaturesById(id)
            ?: throw BookCreatureNotFoundException("BookCreature with id $id not found")
        println(userService.findAdmins().contains(bookCreature.user))
        println(userService.findAdmins())
        println(bookCreature.user)
        val authUser = jwtService.getUsername(jwtService.extractToken(token))
        if (bookCreature.user.username != authUser && userService.getUserByUsername(authUser).role != Role.ROLE_ADMIN) {
            throw AccessDeniedException("You are not allowed to delete this book creature")
        }
        bookCreatureRepository.delete(bookCreature)
        coordinatesService.deleteCoordinates(bookCreature.coordinates.id)
        ringService.grabRingFromCreature(bookCreature.ring)
    }

    fun updateBookCreature(id: Long, bookCreatureDto: BookCreatureDto, token: String): BookCreatureResponseDto {
        val bookCreature = bookCreatureRepository.findBookCreaturesById(id)
            ?: throw BookCreatureNotFoundException("BookCreature with id $id not found")
        val authUser = jwtService.getUsername(jwtService.extractToken(token))
        if (bookCreature.user.username != authUser && userService.getUserByUsername(authUser).role != Role.ROLE_ADMIN) {
            throw AccessDeniedException("You are not allowed to update this book creature")
        }
        bookCreature.name = bookCreatureDto.name
        bookCreature.coordinates =
            coordinatesService.updateCoordinates(bookCreature.coordinates.id, bookCreatureDto.coordinates)
        bookCreature.age = bookCreatureDto.age
        bookCreature.creatureType = BookCreatureType.valueOf(bookCreatureDto.creatureType)
        bookCreature.creatureLocation =
            magicCityService.updateMagicCity(bookCreature.creatureLocation.id, bookCreatureDto.creatureLocation, token)
        if (bookCreatureRepository.findByRingId(bookCreature.ring.id)?.let { it.id != bookCreature.id } == true)
            throw RingAlreadyOwnedException("Ring ${bookCreature.ring.name} already has an owner")
        bookCreature.ring = ringService.updateRing(bookCreature.ring.id, bookCreatureDto.ring, token)
        bookCreature.attackLevel = bookCreatureDto.attackLevel
        bookCreatureRepository.save(bookCreature)
        return createBookCreatureResponseDtoObject(bookCreature)
    }
}
