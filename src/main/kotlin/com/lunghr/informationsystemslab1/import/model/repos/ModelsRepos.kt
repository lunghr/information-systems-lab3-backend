package com.lunghr.informationsystemslab1.import.model.repos

import com.lunghr.informationsystemslab1.import.model.FileStats
import com.lunghr.informationsystemslab1.model.BookCreature
import com.lunghr.informationsystemslab1.model.BookCreatureType
import com.lunghr.informationsystemslab1.model.Coordinates
import com.lunghr.informationsystemslab1.model.MagicCity
import com.lunghr.informationsystemslab1.model.Ring
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FileStatsRepository : JpaRepository<FileStats, Long> {

}
