package org.example.sso.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@Slf4j
public class LoggingOidcUserService extends OidcUserService {
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        log.info("=== UserInfo URL: {}", userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
        log.info("=== Access token: {}", userRequest.getAccessToken().getTokenValue());
        return super.loadUser(userRequest);
    }
}
