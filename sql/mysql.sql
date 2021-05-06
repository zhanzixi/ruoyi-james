-- ----------------------------
-- 1、部门表
-- ----------------------------
drop table if exists sys_dept;
create table sys_dept
(
    id           int unsigned     not null auto_increment primary key comment '部门id',
    parent_id    int unsigned     not null default 0 comment '父部门id',
    name         varchar(30)      not null comment '部门名称',
    sort         tinyint unsigned not null default 0 comment '显示顺序',
    leader       varchar(20)      null comment '负责人',
    phone        varchar(11)      null comment '联系电话',
    email        varchar(50)      null comment '邮箱',
    status       tinyint unsigned not null default 0 comment '部门状态（0正常 1停用）',
    is_deleted   tinyint unsigned not null default 0 comment '删除标志（0代表存在 1代表删除）',
    create_by    varchar(64)      null comment '创建者',
    modified_by  varchar(64)      null comment '更新者',
    gmt_create   datetime         not null default now() comment '创建时间',
    gmt_modified datetime         null on update now() comment '更新时间'
) comment = '部门表';

insert into sys_dept(name)
values ('集团公司');
-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user
(
    id           int unsigned     not null auto_increment primary key comment '用户ID',
    dept_id      bigint(20)       null comment '部门ID',
    name         varchar(30)      not null comment '用户账号',
    nickname     varchar(30)      not null comment '用户昵称',
    type         varchar(2)       null comment '用户类型（00系统用户）',
    email        varchar(50)      null comment '用户邮箱',
    phone_number varchar(11)      null comment '手机号码',
    sex          tinyint unsigned null comment '用户性别（0男 1女 2未知）',
    avatar       varchar(100)     null comment '头像地址',
    password     varchar(100)     not null comment '密码',
    remark       varchar(500)     null comment '备注',
    status       tinyint unsigned not null default 0 comment '帐号状态（0正常 1停用）',
    is_deleted   tinyint unsigned not null default 0 comment '删除标志（0代表存在 1代表删除）',
    login_ip     varchar(50)      null comment '最后登录IP',
    gmt_login    datetime         null comment '最后登录时间',
    create_by    varchar(64)      null comment '创建者',
    modified_by  varchar(64)      null comment '更新者',
    gmt_create   datetime         null     default now() comment '创建时间',
    gmt_modified datetime         null on update now() comment '更新时间',
    unique key (name, is_deleted)
) comment = '用户信息表';

insert into sys_user(name, nickname, password)
values ('admin', '超级管理员', 'e10adc3949ba59abbe56e057f20f883e'),
       ('james', '詹姆士', 'e10adc3949ba59abbe56e057f20f883e');

-- ----------------------------
-- 3、岗位信息表
-- ----------------------------
drop table if exists sys_post;
create table sys_post
(
    id           int unsigned     not null auto_increment primary key comment '岗位ID',
    code         varchar(64)      not null comment '岗位编码',
    name         varchar(50)      not null comment '岗位名称',
    sort         tinyint unsigned not null default 0 comment '显示顺序',
    status       tinyint unsigned not null default 0 comment '状态（0正常 1停用）',
    remark       varchar(500)     null comment '备注',
    create_by    varchar(64)      null comment '创建者',
    modified_by  varchar(64)      null comment '更新者',
    gmt_create   datetime         null     default now() comment '创建时间',
    gmt_modified datetime         null on update now() comment '更新时间',
    unique key (code)
) comment = '岗位信息表';
-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------
insert into sys_post(code, name)
values ('ceo', '董事长'),
       ('se', '项目经理'),
       ('hr', '人力资源'),
       ('user', '普通员工');

-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role
(
    id           int unsigned     not null auto_increment primary key comment '角色ID',
    name         varchar(30)      not null comment '角色名称',
    code         varchar(100)     not null comment '角色权限字符串',
    sort         tinyint unsigned not null default 0 comment '显示顺序',
    data_scope   tinyint unsigned not null default 1 comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    status       tinyint unsigned not null default 0 comment '角色状态（0正常 1停用）',
    is_deleted   tinyint unsigned not null default 0 comment '删除标志（0代表存在 2代表删除）',
    remark       varchar(500)     null comment '备注',
    create_by    varchar(64)      null comment '创建者',
    modified_by  varchar(64)      null comment '更新者',
    gmt_create   datetime         null     default now() comment '创建时间',
    gmt_modified datetime         null on update now() comment '更新时间'
) comment = '角色信息表';

insert into sys_role(name, code)
values ('超级管理员', 'admin'),
       ('普通角色', 'common');

-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu
(
    id           int unsigned     not null auto_increment primary key comment '菜单ID',
    parent_id    int unsigned     not null default 0 comment '父菜单ID',
    name         varchar(50)      not null comment '菜单名称',
    sort         tinyint unsigned not null default 0 comment '显示顺序',
    path         varchar(200)     null comment '路由地址',
    component    varchar(255)     null comment '组件路径',
    is_frame     tinyint unsigned not null default 0 comment '是否为外链（0否 1是）',
    is_cache     tinyint unsigned null     default 0 comment '是否缓存（0不缓存 1缓存）',
    type         tinyint unsigned not null default 2 comment '菜单类型（1目录 2菜单 3按钮）',
    visible      tinyint unsigned not null default 1 comment '菜单状态（0隐藏 1显示）',
    status       tinyint unsigned not null default 0 comment '菜单状态（0正常 1停用）',
    perms        varchar(100)     null comment '权限标识',
    icon         varchar(100)     null comment '菜单图标',
    remark       varchar(500)     null comment '备注',
    create_by    varchar(64)      null comment '创建者',
    modified_by  varchar(64)      null comment '更新者',
    gmt_create   datetime         null     default now() comment '创建时间',
    gmt_modified datetime         null on update now() comment '更新时间'
) comment = '菜单权限表';
insert into sys_menu(parent_id, name, path, type, icon)
values (0, '主页', '/', 2, 'el-icon-s-home'),
       (0, '系统管理', '/system', 1, 'el-icon-setting'),
       (2, '用户管理', '/system/user', 2, 'el-icon-user-solid'),
       (2, '角色管理', '/system/role', 2, 'el-icon-s-check'),
       (2, '菜单管理', '/system/menu', 2, 'el-icon-menu'),
       (2, '部门管理', '/system/dept', 2, 'el-icon-menu'),
       (2, '岗位管理', '/system/post', 2, 'el-icon-postcard');
select *
from sys_menu
order by parent_id, sort;

-- ----------------------------
-- 6、用户和角色关联表  用户N-N角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role
(
    id      int unsigned not null auto_increment primary key comment '自增主键',
    user_id int unsigned not null comment '用户ID',
    role_id int unsigned not null comment '角色ID',
    unique key (user_id, role_id)
) engine = innodb comment = '用户和角色关联表';

insert into sys_user_role(user_id, role_id)
values (1, 1),
       (2, 2);

-- ----------------------------
-- 7、角色和菜单关联表  角色N-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu
(
    id      int unsigned not null auto_increment primary key comment '自增主键',
    role_id int unsigned not null comment '角色ID',
    menu_id int unsigned not null comment '菜单ID',
    unique key (role_id, menu_id)
) comment = '角色和菜单关联表';

-- ----------------------------
-- 8、角色和部门关联表  角色N-N部门
-- ----------------------------
drop table if exists sys_role_dept;
create table sys_role_dept
(
    id      int unsigned not null auto_increment primary key comment '自增主键',
    role_id int unsigned not null comment '角色ID',
    dept_id int unsigned not null comment '部门ID',
    unique key (role_id, dept_id)
) engine = innodb comment = '角色和部门关联表';

-- ----------------------------
-- 9、用户与岗位关联表  用户1-N岗位
-- ----------------------------
drop table if exists sys_user_post;
create table sys_user_post
(
    id      int unsigned not null auto_increment primary key comment 'ID',
    user_id int unsigned not null comment '用户ID',
    post_id int unsigned not null comment '岗位ID',
    unique key (user_id, post_id)
) comment = '用户与岗位关联表';