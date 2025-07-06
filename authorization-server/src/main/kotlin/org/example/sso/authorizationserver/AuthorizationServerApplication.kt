package org.example.sso.authorizationserver

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@MapperScan(basePackages = ["org.example.sso.authorizationserver.mapper"])
@SpringBootApplication
class AuthorizationServerApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<AuthorizationServerApplication>(*args)
        }
    }
}
