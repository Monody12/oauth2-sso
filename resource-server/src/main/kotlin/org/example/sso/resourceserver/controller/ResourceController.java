package org.example.sso.resourceserver.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ResourceController {

    @GetMapping("/resource")
    public Map<String, Object> resource(@AuthenticationPrincipal Jwt jwt) {
        return Collections.singletonMap("message", "Protected resource accessed by: " + jwt.getSubject());
    }

    @GetMapping("/userinfo")
    public Map<String, Object> userInfo(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaims();
    }
}
