package com.lunghr.informationsystemslab1.model.repos

import com.lunghr.informationsystemslab1.model.BookCreature
import com.lunghr.informationsystemslab1.model.Coordinates
import com.lunghr.informationsystemslab1.model.MagicCity
import com.lunghr.informationsystemslab1.model.Ring
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookCreatureRepository : JpaRepository<BookCreature, Long> {
    fun findByUserId(userId: Long): List<BookCreature>?
    fun findBookCreaturesById(id: Long): BookCreature?
    fun findByName(name: String): BookCreature?
    fun findByNameAndUserId(name: String, id: Long): BookCreature?
}

@Repository
interface CoordinatesRepository : JpaRepository<Coordinates, Long>
@Repository
interface RingRepository : JpaRepository<Ring, Long> {
    fun findRingById(id: Long): Ring?
    fun findByName(name: String): Ring?
    fun findByNameAndUserId(name: String, id: Long): Ring?
}

@Repository
interface MagicCityRepository : JpaRepository<MagicCity, Long> {
    fun findMagicCityById(id: Long): MagicCity?

    fun findByName(name: String): MagicCity?
    fun findByNameAndUserId(name: String, id: Long): MagicCity?
}
