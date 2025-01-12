package com.lunghr.informationsystemslab1.service

import com.lunghr.informationsystemslab1.auth.services.JwtService
import com.lunghr.informationsystemslab1.auth.services.UserService
import com.lunghr.informationsystemslab1.dto.RingDto
import com.lunghr.informationsystemslab1.dto.RingResponseDto
import com.lunghr.informationsystemslab1.model.Ring
import com.lunghr.informationsystemslab1.model.repos.RingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ModelsService @Autowired constructor(
    private val ringRepository: RingRepository, private val jwtService: JwtService, private val userService: UserService
) {
    fun saveRing(ringDto: RingDto, token: String): RingResponseDto {
        val ring = Ring(
            name = ringDto.name,
            weight = ringDto.weight,
            user = userService.getUserByUsername(jwtService.getUsername(jwtService.extractToken(token)))
        )
        val savedRing = ringRepository.save(ring)
        return RingResponseDto(
            id = savedRing.id, name = savedRing.name, weight = savedRing.weight, userId = savedRing.user.getId()
        )
    }
}
