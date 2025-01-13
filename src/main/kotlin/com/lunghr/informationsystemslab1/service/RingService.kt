package com.lunghr.informationsystemslab1.service

import com.lunghr.informationsystemslab1.auth.model.ent.Role
import com.lunghr.informationsystemslab1.auth.services.JwtService
import com.lunghr.informationsystemslab1.auth.services.UserService
import com.lunghr.informationsystemslab1.dto.RingDto
import com.lunghr.informationsystemslab1.dto.RingResponseDto
import com.lunghr.informationsystemslab1.model.Ring
import com.lunghr.informationsystemslab1.model.exceptions.AccessDeniedException
import com.lunghr.informationsystemslab1.model.exceptions.RingAlreadyExistsException
import com.lunghr.informationsystemslab1.model.exceptions.RingAlreadyOwnedException
import com.lunghr.informationsystemslab1.model.exceptions.RingNotFoundException
import com.lunghr.informationsystemslab1.model.repos.RingRepository
import com.lunghr.informationsystemslab1.websocket.NotificationHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RingService @Autowired constructor(
    private val ringRepository: RingRepository,
    private val userService: UserService,
    private val jwtService: JwtService,
    private val notificationHandler: NotificationHandler
) {

    fun createRingObject(ringDto: RingDto, token: String): Ring {
        val ring = ringRepository.findByName(ringDto.name)
            ?.let { throw RingAlreadyExistsException("Ring ${ringDto.name} already exists") }
            ?: ringRepository.save(
                Ring(
                    name = ringDto.name,
                    weight = ringDto.weight,
                    user = userService.getUserByUsername(jwtService.getUsername(jwtService.extractToken(token)))
                )
            )
        return ring
    }

    fun createRingResponseDtoObject(ring: Ring): RingResponseDto {
        return RingResponseDto(
            id = ring.id, name = ring.name, weight = ring.weight, userId = ring.user.getId(), ownerless = ring.ownerless
        )
    }

    fun createRing(ringDto: RingDto, token: String): RingResponseDto {
        val ring = createRingObject(ringDto, token)
        notificationHandler.broadcast("Ring ${ring.name} has been created")
        return createRingResponseDtoObject(ring)
    }

    fun getAllRings(): List<RingResponseDto> {
        return ringRepository.findAll().map { ring -> createRingResponseDtoObject(ring) }
    }

    fun getRingById(id: Long): RingResponseDto {
        return ringRepository.findRingById(id)?.let { ring -> createRingResponseDtoObject(ring) }
            ?: throw RingNotFoundException("Ring with id $id not found")
    }

    fun addRingToCreature(ringId: Long, token: String): Ring {
        return ringRepository.findRingById(ringId)
            ?.let { ring ->
                if (ring.ownerless) {
                    ring.ownerless = false
                    notificationHandler.broadcast("Ring ${ring.name} has been added to a creature")
                    ringRepository.save(ring)
                } else {
                    throw RingAlreadyOwnedException("Ring with id $ringId already has an owner")
                }
            } ?: throw RingNotFoundException("Ring with id $ringId not found")
    }

    fun grabRingFromCreature(ring: Ring): Ring {
        return ringRepository.findByName(ring.name)
            ?.let {
                if (it.ownerless) {
                    throw RingAlreadyOwnedException("Ring ${it.name} already has no owner")
                } else {
                    it.ownerless = true
                    notificationHandler.broadcast("Ring ${it.name} has been removed from a creature")
                    ringRepository.save(it)
                }
            } ?: throw RingNotFoundException("Ring ${ring.name} not found")
    }

    fun deleteRingById(id: Long, token: String) {
        ringRepository.findRingById(id)?.let { ring ->
            val authUser = jwtService.getUsername(jwtService.extractToken(token))
            if (ring.user.username != authUser && userService.getUserByUsername(authUser).role != Role.ROLE_ADMIN) {
                throw AccessDeniedException("You are not allowed to delete this ring")
            } else if (!ring.ownerless) {
                throw RingAlreadyOwnedException("Ring ${ring.name} has an owner, you are not allowed to delete it")
            }
            notificationHandler.broadcast("Ring ${ring.name} has been deleted")
            ringRepository.delete(ring)
        } ?: throw RingNotFoundException("Ring with id $id not found")
    }

    fun updateRing(id: Long, ringDto: RingDto, token: String): Ring {
        return ringRepository.findRingById(id)
            ?.let { ring ->
                val authUser = jwtService.getUsername(jwtService.extractToken(token))
                if (ring.user.username != authUser && userService.getUserByUsername(authUser).role != Role.ROLE_ADMIN
                ) {
                    throw AccessDeniedException("You are not allowed to update this ring, because you are not the owner")
                }
                ring.name = ringDto.name
                ring.weight = ringDto.weight
                notificationHandler.broadcast("Ring ${ring.name} has been updated")
                ringRepository.save(ring)
            } ?: throw RingNotFoundException("Ring with id $id not found")
    }
}
