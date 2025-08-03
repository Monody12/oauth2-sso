drop table if exists sys_user;

create table sys_user
(
    id            serial
        primary key,
    name          varchar(64)                                                                           not null,
    password      varchar(255),
    phone         varchar(11),
    mobile        varchar(255),
    avatar        varchar(255),
    email         varchar(255),
    id_number     varchar(32),
    status        smallint     default 1                                                                not null,
    create_time   timestamp(6) default timezone('UTC-8'::text, (now())::timestamp(0) without time zone) not null,
    modified_time timestamp(6) default timezone('UTC-8'::text, (now())::timestamp(0) without time zone)
);

comment on table sys_user is '用户表';

comment on column sys_user.id is 'id';

comment on column sys_user.name is '用户名称';

comment on column sys_user.password is '密码';

comment on column sys_user.phone is '手机号(未加密)';

comment on column sys_user.mobile is '手机号(加密)';

comment on column sys_user.avatar is '头像';

comment on column sys_user.email is '邮箱';

comment on column sys_user.id_number is '身份证号';

comment on column sys_user.status is '账号状态(0:无效；1:有效)';

comment on column sys_user.create_time is '创建时间';

comment on column sys_user.modified_time is '修改时间';

alter table sys_user
    owner to postgres;

TRUNCATE TABLE sys_user;

INSERT INTO public.sys_user (id, name, password, phone, mobile, avatar, email, id_number,status, create_time, modified_time)
VALUES (1, 'admin', 'admin', '18011112222', '180******22', 'https://example.com/avatars/default.png',
        'admin@mail.com', '450304200001011234',1, '2025-07-05 22:36:01.000000',
        '2025-07-05 22:36:01.000000');
INSERT INTO public.sys_user (id, name, password, phone, mobile, avatar, email, status, create_time, modified_time)
VALUES (2, 'test', 'test', '17611112233', '176******33', 'https://example.com/avatars/default.png', 'test@mail.com', 1, '2025-08-02 16:44:47.000000',
        '2025-08-02 16:44:47.000000');
