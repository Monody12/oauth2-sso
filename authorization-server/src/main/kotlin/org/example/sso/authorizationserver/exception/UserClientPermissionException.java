package org.example.sso.authorizationserver.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * 用户访问客户端权限异常
 */
public class UserClientPermissionException extends OAuth2AuthenticationException {
    public UserClientPermissionException(String errorCode) {
        super(errorCode);
    }

    public UserClientPermissionException(OAuth2Error error) {
        super(error);
    }
}
