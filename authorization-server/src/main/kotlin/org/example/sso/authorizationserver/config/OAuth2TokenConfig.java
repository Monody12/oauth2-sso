package org.example.sso.authorizationserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.sso.authorizationserver.dto.SysUserDto;
import org.example.sso.authorizationserver.service.AuthServiceImpl;
import org.example.sso.authorizationserver.service.PermissionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class OAuth2TokenConfig {
    private final AuthServiceImpl authService;
    private final ObjectMapper objectMapper;

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return (context) -> {
            // 先取 authentication 与真正的 principal
            Authentication authentication = context.getPrincipal();
            Object principalObj = authentication.getPrincipal();
            SysUserDto user = null;
            if (principalObj instanceof SysUserDto) {
                user = (SysUserDto) principalObj;
            }

            // Access Token 自定义（放 List 而不是 Set）
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                String clientId = context.getRegisteredClient().getClientId();
                Set<GrantedAuthority> authorities = authService.getAuthoritiesForUserInSystem(user.getId(), clientId);
                List<String> authoritiesList = authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
                context.getClaims().claim("authorities", authoritiesList);
                // 其它想放的 claim（注意类型必须可序列化为 JSON）
            }

            // ID Token 自定义（正确判断 id_token）
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                // 构造标准 OIDC claims（至少 sub）
                OidcUserInfo.Builder userInfoBuilder = getBuilder(context, user);

                // 注意：ID Token 的 claim names 有标准常量（IdTokenClaimNames）
                Map<String, Object> idClaims = userInfoBuilder.build().getClaims();

                // 如果你希望强制设置 sub 使用 IdTokenClaimNames.SUB
                if (!idClaims.containsKey(IdTokenClaimNames.SUB)) {
                    idClaims.put(IdTokenClaimNames.SUB, user.getUsername());
                }

                context.getClaims().claims(claims -> claims.putAll(idClaims));
            }
        };
    }

    private static OidcUserInfo.Builder getBuilder(JwtEncodingContext context, SysUserDto user) {
        Set<String> authorizedScopes = context.getAuthorizedScopes();

        OidcUserInfo.Builder userInfoBuilder = OidcUserInfo.builder();
        // sub
        userInfoBuilder.subject(user.getUsername());
        // profile scope
        if (authorizedScopes.contains(OidcScopes.PROFILE)) {
            userInfoBuilder.preferredUsername(user.getNickname());
            userInfoBuilder.claim("avatar", user.getAvatar());
        }
        if (authorizedScopes.contains(OidcScopes.EMAIL)) {
            userInfoBuilder.email(user.getEmail());
            userInfoBuilder.emailVerified(true);
        }
        if (authorizedScopes.contains(OidcScopes.PHONE)) {
            userInfoBuilder.phoneNumber(user.getPhone());
            userInfoBuilder.phoneNumberVerified(false);
        }
        if (authorizedScopes.contains("company")) {
            userInfoBuilder.claim("department_id", user.getDepartmentId());
        }
        return userInfoBuilder;
    }
}
