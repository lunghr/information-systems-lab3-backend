package com.lunghr.informationsystemslab1.auth.services

import com.lunghr.informationsystemslab1.auth.model.dto.RequestResponseDto
import com.lunghr.informationsystemslab1.auth.model.dto.UserResponseDto
import com.lunghr.informationsystemslab1.auth.model.ent.RequestStatus
import com.lunghr.informationsystemslab1.auth.model.repos.RequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AdminService @Autowired constructor(
    private val requestService: RequestService,
    private val userService: UserService,
    private val requestRepository: RequestRepository
) {
    fun approveRequest(id: Long) {
        requestRepository.findById(id).let { request ->
            request.ifPresent { req ->
                userService.makeAdmin(req.username)
                requestService.changeStatus(id, RequestStatus.APPROVED)
            }
        }
    }

    fun rejectRequest(id: Long) {
        requestRepository.findById(id).let { request ->
            request.ifPresent {
                requestService.changeStatus(id, RequestStatus.REJECTED)
            }
        }
    }

    fun getRequests(): List<RequestResponseDto> {
        return requestRepository.findAll().let { requests ->
            requests.map { request ->
                RequestResponseDto(
                    id = request.id,
                    userId = request.user.getId(),
                    username = request.username,
                    status = request.status,
                    role = request.user.role
                )
            }
        }
    }

    fun getAdmins(): List<UserResponseDto> = userService.findAdmins().let { admins ->
        admins.map { admin ->
            UserResponseDto(
                id = admin.getId(),
                username = admin.username,
                role = admin.role
            )
        }
    }

    fun getUsers(): List<UserResponseDto> = userService.findAll().let { users ->
        users.map { user ->
            UserResponseDto(
                id = user.getId(),
                username = user.username,
                role = user.role
            )
        }
    }


}
