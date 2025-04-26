package org.example.sso.authorizationserver.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ResourceController {
    @GetMapping("/principal")
    fun getPrincipal(): Any {
        return SecurityContextHolder.getContext().authentication.principal
    }
}