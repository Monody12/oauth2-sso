package org.example.sso.authorizationserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;


/**
 * 用户扩展字段（不序列化会抛异常(@JsonSerialize,Serializable),不将扩展字段忽略也会有异常[@JsonIgnoreProperties(ignoreUnknown = true)] 是因为 security 内部实现的原因）
 *
 * @author byh
 * @date 2023-09-15
 * @description
 */
@Data
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUserDto implements UserDetails, Serializable {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    /**
     * id
     */
    private Integer id;
    /**
     * 手机号(未加密)
     */
    private String phone;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 部门id（用户在公司内部的直属部门id）
     */
    private Integer departmentId;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 身份证号
     */
    private String idNumber;
    /**
     * 账号状态(0:无效；1:有效)
     */
    private Integer status;
    /**
     * 权限
     */
    private Collection<GrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
