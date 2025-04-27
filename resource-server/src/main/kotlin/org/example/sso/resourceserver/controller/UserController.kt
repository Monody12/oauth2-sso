package org.example.sso.resourceserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(("/api/user"))
class UserController {
    @GetMapping
    fun getUser(): ResponseEntity<Any> {
        return ResponseEntity.ok(
            "user"
        )
    }
}