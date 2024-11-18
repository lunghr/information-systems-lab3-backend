package com.lunghr.informationsystemslab1.dto

import com.lunghr.informationsystemslab1.models.Ring
import org.springframework.data.jpa.repository.JpaRepository

interface RingRepository : JpaRepository<Ring, Int>
