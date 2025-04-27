package org.example.sso.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClientApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<ClientApplication>(*args)
        }
    }
}


