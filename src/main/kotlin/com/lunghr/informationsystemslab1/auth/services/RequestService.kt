package com.lunghr.informationsystemslab1.auth.services

import com.lunghr.informationsystemslab1.auth.model.ent.Request
import com.lunghr.informationsystemslab1.auth.model.ent.RequestStatus
import com.lunghr.informationsystemslab1.auth.model.repos.RequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RequestService @Autowired constructor(
    private val jwtService: JwtService,
    private val userService: UserService,
    private val requestRepository: RequestRepository
) {

    fun changeStatus(id: Long, status: RequestStatus) {
        requestRepository.findById(id).ifPresent { request ->
            request.status = status
            requestRepository.save(request)
        }
    }

    fun createRequest(token: String): Request {
        return Request(
            username = jwtService.getUsername(jwtService.extractToken(token)),
            user = userService.getUserByUsername(jwtService.getUsername(jwtService.extractToken(token)))
        )
    }

    fun requestAdmin(token: String) {
        if (userService.findAdmins().isEmpty()) {
            val username = jwtService.getUsername(jwtService.extractToken(token))
            userService.makeAdmin(username)
        } else {
            requestRepository.save(createRequest(token))
        }
    }
}
