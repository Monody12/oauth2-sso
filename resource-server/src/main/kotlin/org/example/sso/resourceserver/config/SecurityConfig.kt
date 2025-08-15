package org.example.sso.resourceserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import java.util.stream.Collectors
import kotlin.collections.emptyList


@EnableWebSecurity(debug = false)
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

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        // 创建一个 GrantedAuthoritiesConverter 用于转换权限
        val grantedAuthoritiesConverter: Converter<Jwt?, MutableCollection<GrantedAuthority?>?> = Converter { jwt ->
            // 从 JWT 的 "authorities" claim 中获取权限列表
            val authorities: List<String?> =
                jwt.getClaimAsStringList("authorities") ?: // 如果 "authorities" claim 不存在，返回空集合
                return@Converter mutableListOf<GrantedAuthority?>()
            authorities.stream()
                .map<SimpleGrantedAuthority?> { role: String? -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList())
        }

        val converter = JwtAuthenticationConverter()
        // 设置自定义的权限转换器
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter)
        return converter
    }

}