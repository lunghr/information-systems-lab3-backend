package com.lunghr.informationsystemslab1.service

import com.lunghr.informationsystemslab1.auth.model.ent.User
import com.lunghr.informationsystemslab1.auth.services.UserService
import com.lunghr.informationsystemslab1.dto.RingDto
import com.lunghr.informationsystemslab1.model.Ring
import com.lunghr.informationsystemslab1.model.repos.RingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ModelsService @Autowired constructor(
    private val ringRepository: RingRepository
){
    fun saveRing(ringDto: RingDto, user: User): Ring {
    val ring = Ring(
        name = ringDto.name,
        weight = ringDto.weight,
        user = user
    )
    return ringRepository.save(ring)}
}


