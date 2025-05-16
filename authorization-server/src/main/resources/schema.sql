-- Spring Security User Schema for JdbcUserDetailsManager

-- 用户表
CREATE TABLE users (
                       username VARCHAR_IGNORECASE(50) NOT NULL PRIMARY KEY,
                       password VARCHAR_IGNORECASE(500) NOT NULL, -- 长度应足够存储BCrypt哈希 (通常60字符)
                       enabled BOOLEAN NOT NULL
);

-- 用户权限表
CREATE TABLE authorities (
                             username VARCHAR_IGNORECASE(50) NOT NULL,
                             authority VARCHAR_IGNORECASE(50) NOT NULL,
                             CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);
CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

-- 注册客户端
CREATE TABLE oauth2_client (
                               id VARCHAR(255) NOT NULL,
                               client_id VARCHAR(255) NOT NULL,
                               client_id_issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
                               client_secret VARCHAR(255) DEFAULT NULL,
                               client_secret_expires_at TIMESTAMP DEFAULT NULL,
                               client_name VARCHAR(255) NOT NULL,
                               client_authentication_methods VARCHAR(1000) NOT NULL,
                               authorization_grant_types VARCHAR(1000) NOT NULL,
                               redirect_uris VARCHAR(1000) DEFAULT NULL,
                               post_logout_redirect_uris VARCHAR(1000) DEFAULT NULL,
                               scopes VARCHAR(1000) NOT NULL,
                               client_settings VARCHAR(2000) NOT NULL,
                               token_settings VARCHAR(2000) NOT NULL,
                               PRIMARY KEY (id)
);

-- 授权记录表
CREATE TABLE oauth2_authorization (
                                      id varchar(255) NOT NULL,
                                      registered_client_id varchar(255) NOT NULL,
                                      principal_name varchar(255) NOT NULL,
                                      authorization_grant_type varchar(255) NOT NULL,
                                      authorized_scopes varchar(1000) DEFAULT NULL,
                                      attributes varchar(4000) DEFAULT NULL,
                                      state varchar(500) DEFAULT NULL,
                                      authorization_code_value varchar(4000) DEFAULT NULL,
                                      authorization_code_issued_at timestamp DEFAULT NULL,
                                      authorization_code_expires_at timestamp DEFAULT NULL,
                                      authorization_code_metadata varchar(2000) DEFAULT NULL,
                                      access_token_value varchar(4000) DEFAULT NULL,
                                      access_token_issued_at timestamp DEFAULT NULL,
                                      access_token_expires_at timestamp DEFAULT NULL,
                                      access_token_metadata varchar(2000) DEFAULT NULL,
                                      access_token_type varchar(255) DEFAULT NULL,
                                      access_token_scopes varchar(1000) DEFAULT NULL,
                                      refresh_token_value varchar(4000) DEFAULT NULL,
                                      refresh_token_issued_at timestamp DEFAULT NULL,
                                      refresh_token_expires_at timestamp DEFAULT NULL,
                                      refresh_token_metadata varchar(2000) DEFAULT NULL,
                                      oidc_id_token_value varchar(4000) DEFAULT NULL,
                                      oidc_id_token_issued_at timestamp DEFAULT NULL,
                                      oidc_id_token_expires_at timestamp DEFAULT NULL,
                                      oidc_id_token_metadata varchar(2000) DEFAULT NULL,
                                      oidc_id_token_claims varchar(2000) DEFAULT NULL,
                                      user_code_value varchar(4000) DEFAULT NULL,
                                      user_code_issued_at timestamp DEFAULT NULL,
                                      user_code_expires_at timestamp DEFAULT NULL,
                                      user_code_metadata varchar(2000) DEFAULT NULL,
                                      device_code_value varchar(4000) DEFAULT NULL,
                                      device_code_issued_at timestamp DEFAULT NULL,
                                      device_code_expires_at timestamp DEFAULT NULL,
                                      device_code_metadata varchar(2000) DEFAULT NULL,
                                      PRIMARY KEY (id)
);

-- 授权同意信息表
CREATE TABLE oauth2_authorization_consent (
                                              registered_client_id varchar(255) NOT NULL,
                                              principal_name varchar(255) NOT NULL,
                                              authorities varchar(1000) NOT NULL,
                                              PRIMARY KEY (registered_client_id, principal_name)
);