package org.example.sso.authorizationserver;

import lombok.extern.slf4j.Slf4j;
import org.example.sso.authorizationserver.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@SpringBootTest
public class AuthorizationServerApplicationTest {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        UserDetails admin = userDetailsService.loadUserByUsername("admin");
        log.info(admin.toString());
    }
}
