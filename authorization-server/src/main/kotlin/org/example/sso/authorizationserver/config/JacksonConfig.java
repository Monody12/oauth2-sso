package org.example.sso.authorizationserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sso.authorizationserver.entity.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jackson2.SecurityJackson2Modules;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 获取 Spring Security 的 ClassLoader
        ClassLoader classLoader = JacksonConfig.class.getClassLoader();
        // 加载所有 Spring Security 相关的 Jackson 模块
        var securityModules = SecurityJackson2Modules.getModules(classLoader);

        objectMapper.registerModules(securityModules);

        objectMapper.addMixIn(CustomUserDetails.class, CustomUserDetailsMixin.class);

        return objectMapper;
    }
}
