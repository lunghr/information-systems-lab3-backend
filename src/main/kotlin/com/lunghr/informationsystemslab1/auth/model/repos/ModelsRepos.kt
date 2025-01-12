package com.lunghr.informationsystemslab1.auth.model.repos

import com.lunghr.informationsystemslab1.model.BookCreature
import com.lunghr.informationsystemslab1.model.Coordinates
import com.lunghr.informationsystemslab1.model.MagicCity
import com.lunghr.informationsystemslab1.model.Ring
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookCreatureRepository : JpaRepository<BookCreature, Long>

@Repository
interface CoordinatesRepository : JpaRepository<Coordinates, Long> {
    fun findByCreatureId(creatureId: Long): List<Coordinates>?
//    fun findByUserId(userId: Long): Coordinates?
}

@Repository
interface RingRepository : JpaRepository<Ring, Long> {
    fun findByUserId(userId: Long): List<Ring>?
//    fun fndByCreaturesId(creaturesId: Long): Ring?
}

@Repository
interface MagicCityRepository : JpaRepository<MagicCity, Long> {
    fun findByUserId(userId: Long): List<MagicCity>?
//    fun findByCreatureId(creatureId: Long): MagicCity?
}
