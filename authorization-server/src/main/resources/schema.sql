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
CREATE TABLE oauth2_registered_client (
                                          id VARCHAR(100) NOT NULL,
                                          client_id VARCHAR(100) NOT NULL,
                                          client_id_issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                          client_secret VARCHAR(200) DEFAULT NULL, -- 长度应足够存储BCrypt哈希
                                          client_secret_expires_at TIMESTAMP DEFAULT NULL,
                                          client_name VARCHAR(200) NOT NULL,
                                          client_authentication_methods VARCHAR(1000) NOT NULL,
                                          authorization_grant_types VARCHAR(1000) NOT NULL,
                                          redirect_uris VARCHAR(1000) DEFAULT NULL,
                                          post_logout_redirect_uris VARCHAR(1000) DEFAULT NULL, -- OIDC RP-Initiated Logout
                                          scopes VARCHAR(1000) NOT NULL,
                                          client_settings VARCHAR(2000) NOT NULL, -- 存储为JSON字符串
                                          token_settings VARCHAR(2000) NOT NULL, -- 存储为JSON字符串
                                          PRIMARY KEY (id)
);
CREATE UNIQUE INDEX oauth2_registered_client_client_id_idx ON oauth2_registered_client (client_id);

-- 授权记录表
CREATE TABLE oauth2_authorization (
                                      id VARCHAR(100) NOT NULL,
                                      registered_client_id VARCHAR(100) NOT NULL,
                                      principal_name VARCHAR(200) NOT NULL,
                                      authorization_grant_type VARCHAR(100) NOT NULL,
                                      authorized_scopes VARCHAR(1000) DEFAULT NULL,
                                      attributes TEXT DEFAULT NULL, -- 存储为JSON/BLOB
                                      state VARCHAR(500) DEFAULT NULL,
                                      authorization_code_value TEXT DEFAULT NULL, -- BLOB for H2/MySQL
                                      authorization_code_issued_at TIMESTAMP DEFAULT NULL,
                                      authorization_code_expires_at TIMESTAMP DEFAULT NULL,
                                      authorization_code_metadata TEXT DEFAULT NULL, -- 存储为JSON/BLOB
                                      access_token_value TEXT DEFAULT NULL, -- BLOB for H2/MySQL
                                      access_token_issued_at TIMESTAMP DEFAULT NULL,
                                      access_token_expires_at TIMESTAMP DEFAULT NULL,
                                      access_token_metadata TEXT DEFAULT NULL, -- 存储为JSON/BLOB
                                      access_token_type VARCHAR(100) DEFAULT NULL,
                                      access_token_scopes VARCHAR(1000) DEFAULT NULL,
                                      oidc_id_token_value TEXT DEFAULT NULL, -- BLOB for H2/MySQL
                                      oidc_id_token_issued_at TIMESTAMP DEFAULT NULL,
                                      oidc_id_token_expires_at TIMESTAMP DEFAULT NULL,
                                      oidc_id_token_metadata TEXT DEFAULT NULL, -- 存储为JSON/BLOB
                                      oidc_id_token_claims TEXT DEFAULT NULL, -- 存储为JSON/BLOB, OIDC Claims
                                      refresh_token_value TEXT DEFAULT NULL, -- BLOB for H2/MySQL
                                      refresh_token_issued_at TIMESTAMP DEFAULT NULL,
                                      refresh_token_expires_at TIMESTAMP DEFAULT NULL,
                                      refresh_token_metadata TEXT DEFAULT NULL, -- 存储为JSON/BLOB
                                      user_code_value TEXT DEFAULT NULL,          -- BLOB, OAuth 2.0 Device Authorization Grant
                                      user_code_issued_at TIMESTAMP DEFAULT NULL,
                                      user_code_expires_at TIMESTAMP DEFAULT NULL,
                                      user_code_metadata TEXT DEFAULT NULL,       -- 存储为JSON/BLOB
                                      device_code_value TEXT DEFAULT NULL,        -- BLOB, OAuth 2.0 Device Authorization Grant
                                      device_code_issued_at TIMESTAMP DEFAULT NULL,
                                      device_code_expires_at TIMESTAMP DEFAULT NULL,
                                      device_code_metadata TEXT DEFAULT NULL,     -- 存储为JSON/BLOB
                                      PRIMARY KEY (id)
);


-- 授权同意信息表
CREATE TABLE oauth2_authorization_consent (
                                              registered_client_id VARCHAR(100) NOT NULL,
                                              principal_name VARCHAR(200) NOT NULL,
                                              authorities VARCHAR(1000) NOT NULL, -- 存储用户同意的权限范围，逗号分隔
                                              PRIMARY KEY (registered_client_id, principal_name)
);