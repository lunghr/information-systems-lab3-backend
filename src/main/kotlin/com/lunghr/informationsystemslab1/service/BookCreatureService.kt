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
import com.lunghr.informationsystemslab1.websocket.NotificationHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookCreatureService @Autowired constructor(
    private val bookCreatureRepository: BookCreatureRepository,
    private val userService: UserService,
    private val jwtService: JwtService,
    private val magicCityService: MagicCityService,
    private val ringService: RingService,
    private val coordinatesService: CoordinatesService,
    private val notificationHandler: NotificationHandler
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
                    creatureLocation = magicCityService.addCreatureToCity(bookCreatureDto.creatureLocationId, token),
                    ring = ringService.addRingToCreature(bookCreatureDto.ringId, token),
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
        notificationHandler.broadcast("BookCreature ${bookCreature.name} has been created")
        return createBookCreatureResponseDtoObject(bookCreature)
    }

    fun validateBookCreatureUserAccess(id: Long, token: String): BookCreature {
        val bookCreature = bookCreatureRepository.findBookCreatureById(id)
            ?: throw BookCreatureNotFoundException("BookCreature with id $id not found")
        val authUser = jwtService.getUsername(jwtService.extractToken(token))
        if (bookCreature.user.username != authUser && userService.getUserByUsername(authUser).role != Role.ROLE_ADMIN) {
            throw AccessDeniedException("You are not allowed to update this book creature")
        }
        return bookCreature
    }

    fun getAllBookCreatures(): List<BookCreatureResponseDto> {
        return bookCreatureRepository.findAll().map { bookCreature -> createBookCreatureResponseDtoObject(bookCreature) }
    }

    fun getBookCreatureById(id: Long): BookCreatureResponseDto {
        return bookCreatureRepository.findBookCreatureById(id)?.let { bookCreature ->
            return createBookCreatureResponseDtoObject(bookCreature)
        }
            ?: throw BookCreatureNotFoundException("BookCreature with id $id not found")
    }

    fun deleteBookCreature(id: Long, token: String) {
        val bookCreature = validateBookCreatureUserAccess(id, token)
        bookCreatureRepository.delete(bookCreature)
        coordinatesService.deleteCoordinates(bookCreature.coordinates.id)
        ringService.grabRingFromCreature(bookCreature.ring)
        notificationHandler.broadcast("BookCreature ${bookCreature.name} has been deleted")
    }

    fun updateBookCreature(id: Long, bookCreatureDto: BookCreatureDto, token: String): BookCreatureResponseDto {
        val bookCreature = validateBookCreatureUserAccess(id, token)
        bookCreature.name = bookCreatureDto.name
        bookCreature.coordinates =
            coordinatesService.updateCoordinates(bookCreature.coordinates.id, bookCreatureDto.coordinates)
        bookCreature.age = bookCreatureDto.age
        bookCreature.creatureType = BookCreatureType.valueOf(bookCreatureDto.creatureType)
        bookCreature.creatureLocation =
            magicCityService.addCreatureToCity(bookCreatureDto.creatureLocationId, token)
        if (bookCreatureDto.ringId != bookCreature.ring.id) {
            if (bookCreatureRepository.findByRingId(bookCreatureDto.ringId)?.let { it.id != bookCreature.id } == true) {
                throw RingAlreadyOwnedException("Ring ${bookCreature.ring.name} already has an owner")
            }
            bookCreature.ring = ringService.addRingToCreature(bookCreatureDto.ringId, token)
        }
        bookCreature.attackLevel = bookCreatureDto.attackLevel
        notificationHandler.broadcast("BookCreature ${bookCreature.name} has been updated")
        bookCreatureRepository.save(bookCreature)
        return createBookCreatureResponseDtoObject(bookCreature)
    }

    fun destroyElfCities(token: String) {
        magicCityService.findElfCities().map { city ->
            magicCityService.getAllCreaturesInCity(city.id).map { creature ->
                try {
                    deleteBookCreature(creature.id, token)
                } catch (e: Exception) {
                    println(e.message)
                }
                magicCityService.deleteMagicCityById(city.id, token)
            }
        }
    }

    fun relocateCreaturesToMordor(token: String) {
        bookCreatureRepository.findBookCreaturesByCreatureType(BookCreatureType.HOBBIT)?.map { hobbit ->
            if (hobbit.user.username == userService.getUserByUsername(
                    jwtService.getUsername(
                            jwtService.extractToken(
                                    token
                                )
                        )
                ).username ||
                userService.getUserByUsername(
                        jwtService.getUsername(
                                jwtService.extractToken(
                                        token
                                    )
                            )
                    ).role == Role.ROLE_ADMIN
            ) {
                hobbit.creatureLocation = magicCityService.addCreatureToMordor(token)
                notificationHandler.broadcast("BookCreature ${hobbit.name} has been relocated to Mordor")
                bookCreatureRepository.save(hobbit)
            }
        }
    }

    fun deleteCreatureByRingId(ringId: Long, token: String) {
        bookCreatureRepository.findByRingId(ringId)?.let { bookCreature ->
            deleteBookCreature(bookCreature.id, token)
        } ?: throw BookCreatureNotFoundException("BookCreature with ring id $ringId not found")
    }

    fun getOldestCreature(): BookCreatureResponseDto {
        return bookCreatureRepository.findBookCreatureWithMaxAge()?.let { bookCreature ->
            return createBookCreatureResponseDtoObject(bookCreature)
        }
            ?: throw BookCreatureNotFoundException("BookCreature with max age not found")
    }

    fun getBookCreatureByNamePart(namePart: String): List<BookCreatureResponseDto> {
        return bookCreatureRepository.findByNameContaining(namePart)?.map { bookCreature ->
            createBookCreatureResponseDtoObject(bookCreature)
        } ?: throw BookCreatureNotFoundException("BookCreature with name containing $namePart not found")
    }
}
