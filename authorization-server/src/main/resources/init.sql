-- =================================================================================================
-- -- 用户权限模型 DDL (PostgreSQL)
-- -- Description: 包含系统、角色、权限、用户及它们之间的关联表，以及一个综合查询视图。
-- =================================================================================================

-- -- 0. 删除已存在的对象 (方便重新执行脚本)
DROP VIEW IF EXISTS vw_user_permissions;
DROP TABLE IF EXISTS tb_role_permission;
DROP TABLE IF EXISTS tb_user_role;
DROP TABLE IF EXISTS tb_system_role;
DROP TABLE IF EXISTS tb_permission;
DROP TABLE IF EXISTS tb_role;
DROP TABLE IF EXISTS tb_system;
DROP TABLE IF EXISTS tb_user;


-- =================================================================================================
-- -- 1. 用户表 (由您提供)
-- =================================================================================================
CREATE TABLE tb_user
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(64) NOT NULL,
    nickname      VARCHAR(64) NOT NULL DEFAULT '',
    department_id INTEGER,
    password      VARCHAR(255),
    phone         VARCHAR(11),
    mobile        VARCHAR(255),
    avatar        VARCHAR(255),
    email         VARCHAR(255),
    id_number     VARCHAR(32),
    status        SMALLINT    NOT NULL DEFAULT 1,
    create_time   TIMESTAMP   NOT NULL DEFAULT NOW(),
    modified_time TIMESTAMP   NOT NULL DEFAULT NOW()
);

-- -- 添加注释
COMMENT ON TABLE tb_user IS '用户表';
COMMENT ON COLUMN tb_user.id IS '主键ID (自增)';
COMMENT ON COLUMN tb_user.username IS '用户名 (登录凭证)';
COMMENT ON COLUMN tb_user.nickname IS '昵称 (显示名)';
COMMENT ON COLUMN tb_user.department_id IS '直属部门（科室、队室）id';
COMMENT ON COLUMN tb_user.password IS '密码 (加密存储)';
COMMENT ON COLUMN tb_user.phone IS '手机号(未加密)';
COMMENT ON COLUMN tb_user.mobile IS '手机号(加密)';
COMMENT ON COLUMN tb_user.avatar IS '头像URL';
COMMENT ON COLUMN tb_user.email IS '邮箱';
COMMENT ON COLUMN tb_user.id_number IS '身份证号';
COMMENT ON COLUMN tb_user.status IS '账号状态(0:禁用; 1:启用)';
COMMENT ON COLUMN tb_user.create_time IS '创建时间';
COMMENT ON COLUMN tb_user.modified_time IS '修改时间';

-- -- 添加索引
CREATE UNIQUE INDEX idx_user_username ON tb_user (username);
CREATE INDEX idx_user_phone ON tb_user (phone);


-- =================================================================================================
-- -- 2. 系统表 (业务系统或应用)
-- =================================================================================================
CREATE TABLE tb_system (
                           id SERIAL PRIMARY KEY,
                           system_code VARCHAR(64) NOT NULL,
                           system_name VARCHAR(128) NOT NULL,
                           description VARCHAR(255),
                           create_time   TIMESTAMP   NOT NULL DEFAULT NOW(),
                           modified_time TIMESTAMP   NOT NULL DEFAULT NOW()
);

-- -- 添加注释
COMMENT ON TABLE tb_system IS '业务系统表';
COMMENT ON COLUMN tb_system.id IS '主键ID (自增)';
COMMENT ON COLUMN tb_system.system_code IS '系统编码 (唯一, 用于程序识别)';
COMMENT ON COLUMN tb_system.system_name IS '系统名称 (用于显示)';
COMMENT ON COLUMN tb_system.description IS '系统描述';
COMMENT ON COLUMN tb_system.create_time IS '创建时间';
COMMENT ON COLUMN tb_system.modified_time IS '修改时间';

-- -- 添加索引
CREATE UNIQUE INDEX idx_system_code ON tb_system (system_code);


-- =================================================================================================
-- -- 3. 角色表
-- =================================================================================================
CREATE TABLE tb_role (
                         id SERIAL PRIMARY KEY,
                         role_code VARCHAR(64) NOT NULL,
                         role_name VARCHAR(128) NOT NULL,
                         description VARCHAR(255),
                         create_time   TIMESTAMP   NOT NULL DEFAULT NOW(),
                         modified_time TIMESTAMP   NOT NULL DEFAULT NOW()
);

-- -- 添加注释
COMMENT ON TABLE tb_role IS '角色表';
COMMENT ON COLUMN tb_role.id IS '主键ID (自增)';
COMMENT ON COLUMN tb_role.role_code IS '角色编码 (唯一, 用于程序识别, 如 ROLE_ADMIN)';
COMMENT ON COLUMN tb_role.role_name IS '角色名称 (用于显示)';
COMMENT ON COLUMN tb_role.description IS '角色描述';
COMMENT ON COLUMN tb_role.create_time IS '创建时间';
COMMENT ON COLUMN tb_role.modified_time IS '修改时间';

-- -- 添加索引
CREATE UNIQUE INDEX idx_role_code ON tb_role (role_code);


-- =================================================================================================
-- -- 4. 权限表
-- =================================================================================================
CREATE TABLE tb_permission (
                               id SERIAL PRIMARY KEY,
                               permission_code VARCHAR(128) NOT NULL,
                               permission_name VARCHAR(128) NOT NULL,
                               description VARCHAR(255)
);

-- -- 添加注释
COMMENT ON TABLE tb_permission IS '权限表';
COMMENT ON COLUMN tb_permission.id IS '主键ID (自增)';
COMMENT ON COLUMN tb_permission.permission_code IS '权限编码 (唯一, 用于后端校验, 如 order:create)';
COMMENT ON COLUMN tb_permission.permission_name IS '权限名称 (用于显示, 如 创建订单)';
COMMENT ON COLUMN tb_permission.description IS '权限描述';

-- -- 添加索引
CREATE UNIQUE INDEX idx_permission_code ON tb_permission (permission_code);


-- =================================================================================================
-- -- 5. 关联关系表
-- =================================================================================================

-- -- 5.1 系统-角色关联表 (一个系统可以有多种角色)
CREATE TABLE tb_system_role (
                                system_id INTEGER NOT NULL,
                                role_id INTEGER NOT NULL,
                                PRIMARY KEY (system_id, role_id),
                                CONSTRAINT fk_system_role_system FOREIGN KEY (system_id) REFERENCES tb_system(id) ON DELETE CASCADE,
                                CONSTRAINT fk_system_role_role FOREIGN KEY (role_id) REFERENCES tb_role(id) ON DELETE CASCADE
);

COMMENT ON TABLE tb_system_role IS '系统-角色关联表';
COMMENT ON COLUMN tb_system_role.system_id IS '系统ID';
COMMENT ON COLUMN tb_system_role.role_id IS '角色ID';

-- -- 5.2 用户-角色关联表 (一个用户可以有多个角色)
CREATE TABLE tb_user_role (
                              user_id INTEGER NOT NULL,
                              role_id INTEGER NOT NULL,
                              PRIMARY KEY (user_id, role_id),
                              CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE,
                              CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES tb_role(id) ON DELETE CASCADE
);

COMMENT ON TABLE tb_user_role IS '用户-角色关联表';
COMMENT ON COLUMN tb_user_role.user_id IS '用户ID';
COMMENT ON COLUMN tb_user_role.role_id IS '角色ID';

-- -- 5.3 角色-权限关联表 (一个角色可以有多个权限)
CREATE TABLE tb_role_permission (
                                    role_id INTEGER NOT NULL,
                                    permission_id INTEGER NOT NULL,
                                    PRIMARY KEY (role_id, permission_id),
                                    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES tb_role(id) ON DELETE CASCADE,
                                    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES tb_permission(id) ON DELETE CASCADE
);

COMMENT ON TABLE tb_role_permission IS '角色-权限关联表';
COMMENT ON COLUMN tb_role_permission.role_id IS '角色ID';
COMMENT ON COLUMN tb_role_permission.permission_id IS '权限ID';


-- =================================================================================================
-- -- 6. 插入示例数据
-- =================================================================================================
-- -- 用户 (来自您提供的数据)
INSERT INTO tb_user (id, username, nickname, password, phone, status) VALUES
                                                                          (1, 'admin', '超级管理员', 'admin', '18011112222', 1),
                                                                          (2, 'test', '业务测试员', 'test', '17611112233', 1)
    ON CONFLICT (id) DO NOTHING;
-- -- 序列需要手动更新，因为我们指定了ID
SELECT setval('tb_user_id_seq', (SELECT MAX(id) FROM tb_user));

-- -- 系统
INSERT INTO tb_system (id, system_code, system_name, description) VALUES
                                                                      (1, 'auth-server', '统一认证授权中心', '管理所有用户、角色、权限的中心服务'),
                                                                      (2, 'order-system', '订单管理系统', '企业的核心订单业务处理系统'),
                                                                      (3, 'crm-system', '客户关系管理系统', '管理客户信息与跟进记录')
    ON CONFLICT (id) DO NOTHING;
SELECT setval('tb_system_id_seq', (SELECT MAX(id) FROM tb_system));

-- -- 角色
INSERT INTO tb_role (id, role_code, role_name, description) VALUES
                                                                (1, 'SUPER_ADMIN', '超级管理员', '拥有系统最高权限，可以管理所有内容'),
                                                                (2, 'ORDER_ADMIN', '订单管理员', '管理订单系统的所有订单'),
                                                                (3, 'ORDER_VIEWER', '订单查看员', '只能查看订单，不能进行修改操作'),
                                                                (4, 'CUSTOMER_MANAGER', '客户经理', '负责客户信息的录入和维护')
    ON CONFLICT (id) DO NOTHING;
SELECT setval('tb_role_id_seq', (SELECT MAX(id) FROM tb_role));

-- -- 权限
INSERT INTO tb_permission (id, permission_code, permission_name, description) VALUES
                                                                                  (1, 'system:user:manage', '用户管理', '权限管理后台的用户增删改查'),
                                                                                  (2, 'system:role:manage', '角色管理', '权限管理后台的角色与权限分配'),
                                                                                  (101, 'order:create', '创建订单', '在订单系统创建新订单'),
                                                                                  (102, 'order:read', '查看订单', '在订单系统查看订单详情'),
                                                                                  (103, 'order:update', '修改订单', '在订单系统修改已有订单'),
                                                                                  (104, 'order:delete', '删除订单', '在订单系统删除订单'),
                                                                                  (201, 'crm:customer:create', '新建客户', '在CRM系统创建新客户'),
                                                                                  (202, 'crm:customer:read', '查看客户', '在CRM系统查看客户信息')
    ON CONFLICT (id) DO NOTHING;
SELECT setval('tb_permission_id_seq', (SELECT MAX(id) FROM tb_permission));

-- -- 分配角色到系统
INSERT INTO tb_system_role (system_id, role_id) VALUES
                                                    (1, 1), -- 超级管理员 属于 统一认证授权中心
                                                    (2, 2), -- 订单管理员 属于 订单管理系统
                                                    (2, 3), -- 订单查看员 属于 订单管理系统
                                                    (3, 4)  -- 客户经理 属于 客户关系管理系统
    ON CONFLICT (system_id, role_id) DO NOTHING;

-- -- 分配角色给用户
INSERT INTO tb_user_role (user_id, role_id) VALUES
                                                (1, 1), -- 'admin' 用户是 '超级管理员'
                                                (2, 2), -- 'test' 用户是 '订单管理员'
                                                (2, 4)  -- 'test' 用户同时也是 '客户经理'
    ON CONFLICT (user_id, role_id) DO NOTHING;

-- -- 分配权限给角色
INSERT INTO tb_role_permission (role_id, permission_id) VALUES
-- 超级管理员拥有所有系统管理权限
(1, 1),
(1, 2),
-- 订单管理员拥有所有订单操作权限
(2, 101),
(2, 102),
(2, 103),
(2, 104),
-- 订单查看员只有查看权限
(3, 102),
-- 客户经理拥有CRM的增查权限
(4, 201),
(4, 202)
    ON CONFLICT (role_id, permission_id) DO NOTHING;


-- =================================================================================================
-- -- 7. 创建视图 (用于客户端查询)
-- =================================================================================================
CREATE OR REPLACE VIEW vw_user_permissions AS
SELECT
    u.id AS user_id,
    u.username,
    u.nickname,
    s.id AS system_id,
    s.system_code,
    s.system_name,
    r.id AS role_id,
    r.role_code,
    r.role_name,
    p.id AS permission_id,
    p.permission_code,
    p.permission_name
FROM
    tb_user u
        JOIN
    tb_user_role ur ON u.id = ur.user_id
        JOIN
    tb_role r ON ur.role_id = r.id
        JOIN
    tb_system_role sr ON r.id = sr.role_id
        JOIN
    tb_system s ON sr.system_id = s.id
        JOIN
    tb_role_permission rp ON r.id = rp.role_id
        JOIN
    tb_permission p ON rp.permission_id = p.id
WHERE
    u.status = 1 -- 只查询有效用户的权限
ORDER BY
    u.id, s.id, r.id, p.id;

COMMENT ON VIEW vw_user_permissions IS '用户-系统-角色-权限综合查询视图';

-- -- 视图查询示例
-- -- 查询用户 'test' 的所有权限
-- SELECT * FROM vw_user_permissions WHERE username = 'test';

-- -- 查询 '订单管理系统' 中有哪些用户分别拥有什么权限
-- SELECT username, nickname, role_name, permission_name
-- FROM vw_user_permissions
-- WHERE system_code = 'order-system';