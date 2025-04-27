package org.example.sso.resourceserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity(debug = true)
@Configuration
class SecurityConfig{
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeHttpRequests { authorize -> authorize
                .anyRequest().authenticated()
            }
            .oauth2ResourceServer{oauth2 -> oauth2.jwt(Customizer.withDefaults())}

        return http.build()
    }

}