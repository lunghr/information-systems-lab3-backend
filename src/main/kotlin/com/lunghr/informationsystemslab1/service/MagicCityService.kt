package com.lunghr.informationsystemslab1.service

import com.lunghr.informationsystemslab1.auth.services.JwtService
import com.lunghr.informationsystemslab1.auth.services.UserService
import com.lunghr.informationsystemslab1.dto.MagicCityDto
import com.lunghr.informationsystemslab1.dto.MagicCityResponseDto
import com.lunghr.informationsystemslab1.model.BookCreatureType
import com.lunghr.informationsystemslab1.model.MagicCity
import com.lunghr.informationsystemslab1.model.exceptions.CityAlreadyExistsException
import com.lunghr.informationsystemslab1.model.exceptions.CityNotFoundException
import com.lunghr.informationsystemslab1.model.repos.MagicCityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MagicCityService @Autowired constructor(
    private val magicCityRepository: MagicCityRepository,
    private val jwtService: JwtService,
    private val userService: UserService
) {

    fun createMagicCityObject(magicCityDto: MagicCityDto, token: String): MagicCity {
        val magicCity = magicCityRepository.findByName(magicCityDto.name)
            ?.let { throw CityAlreadyExistsException("City ${magicCityDto.name} already exists") }
            ?: magicCityRepository.save(
                MagicCity(
                    name = magicCityDto.name,
                    area = magicCityDto.area,
                    population = magicCityDto.population,
                    governor = BookCreatureType.valueOf(magicCityDto.governor),
                    capital = magicCityDto.capital,
                    populationDensity = magicCityDto.populationDensity,
                    establishedData = magicCityDto.establishedData,
                    user = userService.getUserByUsername(jwtService.getUsername(jwtService.extractToken(token)))
                )
            )

        return magicCity
    }

    fun createMagicCityResponseDtoObject(magicCity: MagicCity): MagicCityResponseDto {
        return MagicCityResponseDto(
            id = magicCity.id,
            name = magicCity.name,
            area = magicCity.area,
            population = magicCity.population,
            established = magicCity.establishedData,
            governor = magicCity.governor.toString(),
            capital = magicCity.capital,
            populationDensity = magicCity.populationDensity,
            userId = magicCity.user.getId()
        )
    }
    fun createMagicCity(magicCityDto: MagicCityDto, token: String): MagicCityResponseDto {
        val magicCity = createMagicCityObject(magicCityDto, token)
        return createMagicCityResponseDtoObject(magicCity)
    }

    fun getMagicCityByName(magicCityDto: MagicCityDto): MagicCity {
        return magicCityRepository.findByName(magicCityDto.name)
            ?: throw CityNotFoundException("City ${magicCityDto.name} not found")
    }

    fun addCreatureToCity(magicCityDto: MagicCityDto, token: String): MagicCity {
        return magicCityRepository.findByName(magicCityDto.name) ?: createMagicCityObject(magicCityDto, token)
    }
}
