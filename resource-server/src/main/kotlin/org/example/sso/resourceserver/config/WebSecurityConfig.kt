package org.example.sso.resourceserver.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


//@EnableWebSecurity(debug = true)
//@Configuration
class WebSecurityConfig(
    @Value("\${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") var jwkSetUri: String,
){
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeHttpRequests { authorize -> authorize.anyRequest().authenticated() }
            .oauth2ResourceServer { oauth2 -> oauth2.jwt { jwt -> jwt.jwkSetUri(jwkSetUri) } }
        return http.build()
    }

}