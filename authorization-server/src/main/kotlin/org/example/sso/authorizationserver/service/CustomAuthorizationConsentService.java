package org.example.sso.authorizationserver.service;

import org.example.sso.authorizationserver.exception.UserClientPermissionException;
import org.example.sso.authorizationserver.repository.UserClientPermissionRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final OAuth2AuthorizationConsentService delegateService;
    // 用户-客户端权限关系数据源
    private final UserClientPermissionRepository userClientPermissionRepository;

    public CustomAuthorizationConsentService(
            UserClientPermissionRepository userClientPermissionRepository
    ) {
        this.delegateService = new InMemoryOAuth2AuthorizationConsentService();
        this.userClientPermissionRepository = userClientPermissionRepository;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        // 检查用户是否有权限访问该客户端
        String principalName = authorizationConsent.getPrincipalName();
        String clientId = authorizationConsent.getRegisteredClientId();

        if (!userClientPermissionRepository.hasPermission(principalName, clientId)) {
            throw new UserClientPermissionException(
                    new OAuth2Error("access_denied", "User is not allowed to access this client", null));
        }

        delegateService.save(authorizationConsent);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        delegateService.remove(authorizationConsent);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        // 检查用户是否有权限访问该客户端
        if (!userClientPermissionRepository.hasPermission(principalName, registeredClientId)) {
             throw new OAuth2AuthenticationException(
                     new OAuth2Error("access_denied", "User is not permitted to access stored consent for this client.", null));
        }

        return delegateService.findById(registeredClientId, principalName);
    }
}