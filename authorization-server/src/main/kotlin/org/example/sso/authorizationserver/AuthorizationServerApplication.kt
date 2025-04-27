package org.example.sso.authorizationserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthorizationServerApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<AuthorizationServerApplication>(*args)
        }
    }
}
