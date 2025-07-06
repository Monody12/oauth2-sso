package org.example.sso.authorizationserver;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationEndpointConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@EnableWebSecurity(debug = true)
public class AuthServerConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults())
                .authorizationEndpoint(new Customizer<OAuth2AuthorizationEndpointConfigurer>() {
                    @Override
                    public void customize(OAuth2AuthorizationEndpointConfigurer oAuth2AuthorizationEndpointConfigurer) {
                        oAuth2AuthorizationEndpointConfigurer.errorResponseHandler(yourCustomAuthenticationFailureHandler());
                    }
                });
        http.exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
                .cors(Customizer.withDefaults());
        return http.build();
    }

    private AuthenticationFailureHandler yourCustomAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            String ssoErrorPageUrl = "/sso-error"; // 你的SSO错误页面路径
            UriComponentsBuilder errorPageUriBuilder = UriComponentsBuilder.fromPath(ssoErrorPageUrl);

            String clientRedirectUri = request.getParameter(OAuth2ParameterNames.REDIRECT_URI);
            String state = request.getParameter(OAuth2ParameterNames.STATE);

            if (exception instanceof OAuth2AuthenticationException oauth2Ex) {
                OAuth2Error error = oauth2Ex.getError();

                errorPageUriBuilder.queryParam(OAuth2ParameterNames.ERROR, error.getErrorCode());
                if (StringUtils.hasText(error.getDescription())) {
                    errorPageUriBuilder.queryParam(OAuth2ParameterNames.ERROR_DESCRIPTION, error.getDescription());
                }
                // 如果有错误URI，也可以传递
                if (StringUtils.hasText(error.getUri())) {
                    errorPageUriBuilder.queryParam(OAuth2ParameterNames.ERROR_URI, error.getUri());
                }

            } else {
                // 处理其他类型的认证失败
                errorPageUriBuilder.queryParam(OAuth2ParameterNames.ERROR, "authentication_failed");
                String errorMessage = StringUtils.hasText(exception.getMessage()) ?
                        exception.getMessage() : "An unexpected authentication error occurred.";
                errorPageUriBuilder.queryParam(OAuth2ParameterNames.ERROR_DESCRIPTION, errorMessage);
            }

            // 始终尝试传递客户端重定向URI和状态，如果存在的话
            if (StringUtils.hasText(clientRedirectUri)) {
                errorPageUriBuilder.queryParam("client_redirect_uri", // 自定义参数名，以便在错误页面区分
                        URLEncoder.encode(clientRedirectUri, StandardCharsets.UTF_8));
            }
            if (StringUtils.hasText(state)) {
                errorPageUriBuilder.queryParam(OAuth2ParameterNames.STATE, state); // state通常是opaque，直接传递
            }

            // 尝试获取当前已认证的用户名
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
                errorPageUriBuilder.queryParam("username",
                        URLEncoder.encode(authentication.getName(), StandardCharsets.UTF_8));
            }

            response.sendRedirect(errorPageUriBuilder.build().encode().toUriString());
        };
    }

//    @Bean
//    @Order(2)
//    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
//                .formLogin(Customizer.withDefaults());
//        return http.build();
//    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // 允许访问登录页、错误页和静态资源
                        .requestMatchers("/login", "/sso-error", "/assets/**", "/css/**", "/js/**", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults()); // 使用默认登录页
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build());
        manager.createUser(User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build());
        return manager;
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient client1 = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client-1")
                .clientSecret("{noop}secret-1")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://client1.com/login/oauth2/code/client")
                .postLogoutRedirectUri("http://client1.com/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build())
                .build();
        RegisteredClient client2 = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client-2")
                .clientSecret("{noop}secret-2")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://client2.com/login/oauth2/code/client")
                .postLogoutRedirectUri("http://client2.com/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build())
                .build();
        RegisteredClient client3 = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client-3")
                .clientSecret("{noop}secret-3")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://client3.com/login/oauth2/code/client")
                .postLogoutRedirectUri("http://client3.com/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build())
                .build();
        return new InMemoryRegisteredClientRepository(client1, client2, client3);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://sso.com")
                .authorizationEndpoint("/oauth2/authorize")
                .tokenEndpoint("/oauth2/token")
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                .tokenRevocationEndpoint("/oauth2/revoke")
                .jwkSetEndpoint("/oauth2/jwks")
                .oidcUserInfoEndpoint("/userinfo")
                .oidcClientRegistrationEndpoint("/connect/register")
                .build();
    }
}