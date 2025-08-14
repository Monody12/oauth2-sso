package org.example.sso.authorizationserver;

import lombok.extern.slf4j.Slf4j;
import org.example.sso.authorizationserver.service.AuthServiceImpl;
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
    @Autowired
    private AuthServiceImpl authService;

    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        UserDetails admin = userDetailsService.loadUserByUsername("admin");
        log.info(admin.toString());
    }

    @Test
    void getAdminPermissions() {
        var p = authService.getAuthoritiesForUserInSystem(1,"client-1");
        log.info(p.toString());
    }
}
