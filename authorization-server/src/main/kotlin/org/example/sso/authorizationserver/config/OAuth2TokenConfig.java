package org.example.sso.authorizationserver.config;

import org.example.sso.authorizationserver.dto.SysUserDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Set;

@Configuration
public class OAuth2TokenConfig {
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return (context) -> {
            // 仅当令牌是 OIDC ID Token 时才自定义
            if (context.getTokenType().getValue().equals("id_token")) {
                Authentication principal = context.getPrincipal();
                // 假设你的 principal 是一个 UserDetails 对象或自定义的用户对象
                if (principal.getPrincipal() instanceof SysUserDto user) { // 替换成你自己的用户类
                    // 这里你可以从数据库、缓存或其他服务中获取用户信息
                    // CustomUser customUser = userService.findByUsername(user.getUsername());

                    // 获取本次请求被授权的 scopes
                    Set<String> authorizedScopes = context.getAuthorizedScopes();

                    OidcUserInfo.Builder userInfoBuilder = OidcUserInfo.builder();
                    // 基本用户信息
                    userInfoBuilder.subject(user.getUsername());
                    // 头像、昵称（profile）
                    if (authorizedScopes.contains(OidcScopes.PROFILE)) {
                        userInfoBuilder.preferredUsername(user.getNickname());
                        userInfoBuilder.claim("avatar", user.getAvatar());
                    }
                    // 邮箱（标准OIDC声明）
                    if (authorizedScopes.contains(OidcScopes.EMAIL)) {
                        userInfoBuilder.email(user.getEmail());
                        userInfoBuilder.emailVerified(true);
                    }
                    // 手机号（标准OIDC声明）
                    if (authorizedScopes.contains(OidcScopes.PHONE)) {
                        userInfoBuilder.phoneNumber(user.getPhone());
                        userInfoBuilder.phoneNumberVerified(false);
                    }
                    // 部门、角色
                    if (authorizedScopes.contains("company")) {
                        userInfoBuilder.claim("department_id", user.getDepartmentId());
                    }
                    // 将自定义的 UserInfo 设置到上下文中
                    context.getClaims().claims(claims ->
                            claims.putAll(userInfoBuilder.build().getClaims())
                    );
                }
            }
        };
    }
}
