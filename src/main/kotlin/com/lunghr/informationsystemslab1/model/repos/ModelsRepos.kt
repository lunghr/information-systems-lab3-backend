package com.lunghr.informationsystemslab1.model.repos

import com.lunghr.informationsystemslab1.model.BookCreature
import com.lunghr.informationsystemslab1.model.BookCreatureType
import com.lunghr.informationsystemslab1.model.Coordinates
import com.lunghr.informationsystemslab1.model.MagicCity
import com.lunghr.informationsystemslab1.model.Ring
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BookCreatureRepository : JpaRepository<BookCreature, Long> {
    fun findByRingId(id: Long): BookCreature?
    fun findBookCreatureById(id: Long): BookCreature?
    fun findByName(name: String): BookCreature?
    fun findByNameContaining(name: String): List<BookCreature>?
    fun findBookCreaturesByCreatureType(creatureType: BookCreatureType): List<BookCreature>?
    @Query("SELECT bc FROM BookCreature bc WHERE bc.age = (SELECT MAX(bc2.age) FROM BookCreature bc2)")
    fun findBookCreatureWithMaxAge(): BookCreature?
}

@Repository
interface CoordinatesRepository : JpaRepository<Coordinates, Long>
@Repository
interface RingRepository : JpaRepository<Ring, Long> {
    fun findRingById(id: Long): Ring?
    fun findByName(name: String): Ring?
}

@Repository
interface MagicCityRepository : JpaRepository<MagicCity, Long> {
    fun findMagicCityById(id: Long): MagicCity?

    fun findByName(name: String): MagicCity?
    fun findAllByGovernor(governor: BookCreatureType): List<MagicCity>?
}
