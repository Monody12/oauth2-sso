package org.example.sso.authorizationserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.security.Permission;
import java.util.Set;

@Data
@TableName("tb_role")
public class RoleDo {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String roleCode;
    private String roleName;

    // 该字段不属于 tb_role 表，需要手动填充
    @TableField(exist = false)
    private Set<Permission> permissions;
}
