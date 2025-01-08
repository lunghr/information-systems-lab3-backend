package com.lunghr.informationsystemslab1.auth.controllers

import com.lunghr.informationsystemslab1.auth.services.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@Deprecated("This class is deprecated", ReplaceWith("Examples"))
@RestController
@RequestMapping("/example")
class Examples {
    private lateinit var userService: UserService

    @GetMapping
    fun example() :String{
        return "Hello World"
    }

    @GetMapping("/admin")
    fun exampleAdmin() :String{
        return "Hello Admin"
    }

    @GetMapping("/get-admin")
    fun getAdmin() {
        userService.getAdmin()
    }
}