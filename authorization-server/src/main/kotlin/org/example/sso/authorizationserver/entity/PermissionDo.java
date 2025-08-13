package org.example.sso.authorizationserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_permission")
public class PermissionDo {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String permissionCode;
    private String permissionName;
}
