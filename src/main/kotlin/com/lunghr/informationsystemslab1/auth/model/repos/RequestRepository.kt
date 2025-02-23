package com.lunghr.informationsystemslab1.auth.model.repos

import com.lunghr.informationsystemslab1.auth.model.ent.Request
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestRepository : JpaRepository<Request, Long> {
    fun findRequestById(id: Long): Request?
    fun findRequestByUserId(id: Long): Request?
}
