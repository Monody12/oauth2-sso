-- 清空表数据，以便每次重启应用都有一个干净的环境（仅适用于H2内存数据库）
DELETE
FROM oauth2_registered_client;

-- 插入客户端数据
-- 注意：client_secret 应该是经过 BCrypt 加密的。
-- 这里我们为了演示方便，先插入一个明文 'secret-1' 的 BCrypt 加密后的值。
-- 你可以使用在线工具或在代码中生成这个值。例如 '$2a$10$N0tRealPasswordHashForDemo'
-- '$2a$10$rV.hsL8qH/21s5.skT1RgeYCB9aL9OwaFw7W1Y2pXlPAbnZXIv5eS' 是 'secret-1' 的一个 BCrypt 哈希
-- '$2a$10$hWlPz2sH.fEKmO2S7YvB0u9VjsCi1eCscCewp3M823iT16j9RfZge' 是 'secret-2' 的一个 BCrypt 哈希
-- '$2a$10$Qifx.9wrh2wGfQ0x2.JkHe2IvnBIaK2IqT4n1p2X6q9kS2nE2x0jG' 是 'secret-3' 的一个 BCrypt 哈希
INSERT INTO oauth2_registered_client (id, client_id, client_id_issued_at, client_secret, client_name,
                                      client_authentication_methods, authorization_grant_types, redirect_uris,
                                      post_logout_redirect_uris, scopes, client_settings, token_settings)
VALUES ('client-1-id', 'client-1', NOW(), '{noop}secret-1',
        'client-1', 'client_secret_basic', 'authorization_code,refresh_token',
        'http://client1.com/login/oauth2/code/client', 'http://client1.com/', 'openid,profile',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-authorization-consent":true}',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.reuse-refresh-tokens":true}'),
       ('client-2-id', 'client-2', NOW(), '{noop}secret-2',
        'client-2', 'client_secret_basic', 'authorization_code,refresh_token',
        'http://client2.com/login/oauth2/code/client', 'http://client2.com/', 'openid,profile',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-authorization-consent":true}',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.reuse-refresh-tokens":true}'),
       ('client-3-id', 'client-3', NOW(), '{noop}secret-3',
        'client-3', 'client_secret_basic', 'authorization_code,refresh_token',
        'http://client3.com/login/oauth2/code/client', 'http://client3.com/', 'openid,profile',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-authorization-consent":true}',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.reuse-refresh-tokens":true}');


-- copy
-- INSERT INTO oauth2_registered_client (id, client_id, client_id_issued_at, client_secret, client_name,
--                                       client_authentication_methods, authorization_grant_types, redirect_uris,
--                                       post_logout_redirect_uris, scopes, client_settings, token_settings)
-- VALUES ('client-1-id', 'client-1', NOW(), '{bcrypt}$2a$10$rV.hsL8qH/21s5.skT1RgeYCB9aL9OwaFw7W1Y2pXlPAbnZXIv5eS',
--         'client-1', 'client_secret_basic', 'authorization_code,refresh_token',
--         'http://client1.com/login/oauth2/code/client', 'http://client1.com/', 'openid,profile',
--         '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-authorization-consent":true}',
--         '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.reuse-refresh-tokens":true}'),
--        ('client-2-id', 'client-2', NOW(), '{bcrypt}$2a$10$hWlPz2sH.fEKmO2S7YvB0u9VjsCi1eCscCewp3M823iT16j9RfZge',
--         'client-2', 'client_secret_basic', 'authorization_code,refresh_token',
--         'http://client2.com/login/oauth2/code/client', 'http://client2.com/', 'openid,profile',
--         '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-authorization-consent":true}',
--         '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.reuse-refresh-tokens":true}'),
--        ('client-3-id', 'client-3', NOW(), '{bcrypt}$2a$10$Qifx.9wrh2wGfQ0x2.JkHe2IvnBIaK2IqT4n1p2X6q9kS2nE2x0jG',
--         'client-3', 'client_secret_basic', 'authorization_code,refresh_token',
--         'http://client3.com/login/oauth2/code/client', 'http://client3.com/', 'openid,profile',
--         '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-authorization-consent":true}',
--         '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.reuse-refresh-tokens":true}');
--
