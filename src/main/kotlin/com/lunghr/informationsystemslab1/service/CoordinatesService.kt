package com.lunghr.informationsystemslab1.service

import com.lunghr.informationsystemslab1.dto.CoordinatesDto
import com.lunghr.informationsystemslab1.model.Coordinates
import com.lunghr.informationsystemslab1.model.repos.CoordinatesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CoordinatesService @Autowired constructor(
    private val coordinatesRepository: CoordinatesRepository,
) {
    fun saveCoordinates(coordinates: CoordinatesDto): Coordinates {
        return coordinatesRepository.save(
            Coordinates(
                x = coordinates.x,
                y = coordinates.y
            )
        )
    }

    fun deleteCoordinates(id: Long) {
        coordinatesRepository.delete(coordinatesRepository.findById(id).orElseThrow { RuntimeException("Coordinates not found") })
    }

    fun updateCoordinates(id: Long, coordinates: CoordinatesDto): Coordinates {
        return coordinatesRepository.save(
            coordinatesRepository.findById(id).orElseThrow { RuntimeException("Coordinates not found") }.also { coordinatesEntity ->
                coordinatesEntity.x = coordinates.x
                coordinatesEntity.y = coordinates.y
            }
        )
    }
}
