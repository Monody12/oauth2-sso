package org.example.sso.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

@Configuration
public class SecurityConfig {

    /**
     * 配置 RegisteredClientRepository 的 Bean，使用 JDBC 进行持久化。
     * @param jdbcTemplate Spring Boot 自动配置好的 JdbcTemplate Bean。
     * @return JdbcRegisteredClientRepository 实例。
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    /**
     * 配置一个密码编码器，用于加密客户端密钥。
     * 生产环境中必须使用强加密，例如 BCrypt。
     * @return PasswordEncoder 实例。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
