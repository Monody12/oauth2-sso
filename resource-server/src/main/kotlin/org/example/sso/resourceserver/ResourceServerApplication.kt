package org.example.sso.resourceserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ResourceServerApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<ResourceServerApplication>(*args)
        }

    }
}