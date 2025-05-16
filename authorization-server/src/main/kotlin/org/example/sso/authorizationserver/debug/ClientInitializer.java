package org.example.sso.authorizationserver.debug;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClientInitializer implements ApplicationRunner {

    private final RegisteredClientRepository registeredClientRepository;

    public ClientInitializer(RegisteredClientRepository registeredClientRepository) {
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 检查客户端是否已存在，如果不存在则添加
        if (registeredClientRepository.findByClientId("client-1") == null) {
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
            registeredClientRepository.save(client1);
        }

        if (registeredClientRepository.findByClientId("client-2") == null) {
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
            registeredClientRepository.save(client2);
        }

        if (registeredClientRepository.findByClientId("client-3") == null) {
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
            registeredClientRepository.save(client3);
        }
    }
}