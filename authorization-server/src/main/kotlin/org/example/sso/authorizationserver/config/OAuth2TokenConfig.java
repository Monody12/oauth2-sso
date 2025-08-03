package org.example.sso.authorizationserver.config;

import org.example.sso.authorizationserver.dto.SysUserDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

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
                    // 例如：
                    // CustomUser customUser = userService.findByUsername(user.getUsername());

                    OidcUserInfo.Builder userInfoBuilder = OidcUserInfo.builder();

                    // 添加标准的 OIDC 声明
                    userInfoBuilder.subject(user.getUsername());
                    userInfoBuilder.email(user.getEmail()); // 替换为真实邮箱
                    userInfoBuilder.emailVerified(true);
                    userInfoBuilder.phoneNumber(user.getPhone()); // 替换为真实电话
                    userInfoBuilder.phoneNumberVerified(false);

                    // 添加自定义声明
                    userInfoBuilder.claim("avatar", user.getAvatar());
                    userInfoBuilder.claim("id_number", user.getIdNumber());

                    // 将自定义的 UserInfo 设置到上下文中
                    context.getClaims().claims(claims ->
                            claims.putAll(userInfoBuilder.build().getClaims())
                    );
                }
            }
        };
    }
}
