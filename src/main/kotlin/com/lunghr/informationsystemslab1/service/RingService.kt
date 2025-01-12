package com.lunghr.informationsystemslab1.service

import com.lunghr.informationsystemslab1.auth.services.JwtService
import com.lunghr.informationsystemslab1.auth.services.UserService
import com.lunghr.informationsystemslab1.dto.RingDto
import com.lunghr.informationsystemslab1.dto.RingResponseDto
import com.lunghr.informationsystemslab1.model.Ring
import com.lunghr.informationsystemslab1.model.exceptions.RingAlreadyExistsException
import com.lunghr.informationsystemslab1.model.exceptions.RingAlreadyOwnedException
import com.lunghr.informationsystemslab1.model.repos.RingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RingService @Autowired constructor(
    private val ringRepository: RingRepository,
    private val userService: UserService,
    private val jwtService: JwtService
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
            id = ring.id, name = ring.name, weight = ring.weight, userId = ring.user.getId()
        )
    }
    fun createRing(ringDto: RingDto, token: String): RingResponseDto {
        val ring = createRingObject(ringDto, token)
        return createRingResponseDtoObject(ring)
    }

    fun addRingToCreature(ringDto: RingDto, token: String): Ring {
        return ringRepository.findByName(ringDto.name)
            ?.let { ring ->
                if (ring.ownerless) {
                    ring.ownerless = false
                    ringRepository.save(ring)
                } else {
                    throw RingAlreadyOwnedException("Ring ${ringDto.name} already has an owner")
                }
            } ?: createRingObject(ringDto, token)
    }
}
