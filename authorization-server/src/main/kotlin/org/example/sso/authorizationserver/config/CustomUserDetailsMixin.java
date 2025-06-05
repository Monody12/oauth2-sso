package org.example.sso.authorizationserver.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.sso.authorizationserver.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class CustomUserDetailsMixin {

    // --- 用于反序列化 (Deserialization) ---
    // Jackson 将使用此构造函数，并从 JSON 中提取 "user" 字段来创建 CustomUserDetails
    @JsonCreator
    public CustomUserDetailsMixin(
            @JsonProperty("user") User user
    ) {}

    // --- 用于序列化 (Serialization) 和控制反序列化行为 ---

    /**
     * "user" 属性：
     * - 序列化时: 通过 getUser() 获取并写入 JSON。
     * - 反序列化时: 通过 @JsonCreator 构造函数参数 "user" 注入。
     */
    @JsonProperty("user")
    public abstract User getUser();

    /**
     * "authorities" 属性：
     * - 序列化时: 通过 getAuthorities() 获取并写入 JSON。
     * - 反序列化时: 标记为 READ_ONLY，Jackson 不会尝试从 JSON 中读取并设置此属性。
     * 它的值应完全由构造时传入的 'user' 对象决定。
     */
    @JsonProperty(value = "authorities", access = JsonProperty.Access.READ_ONLY)
    public abstract Collection<? extends GrantedAuthority> getAuthorities();

    /**
     * "password" 属性：
     * - 序列化时: 包含。
     * - 反序列化时: READ_ONLY。
     */
    @JsonProperty(value = "password", access = JsonProperty.Access.READ_ONLY)
    public abstract String getPassword();

    /**
     * "username" 属性：
     * - 序列化时: 包含。
     * - 反序列化时: READ_ONLY。
     */
    @JsonProperty(value = "username", access = JsonProperty.Access.READ_ONLY)
    public abstract String getUsername();

    /**
     * "accountNonExpired" 属性：
     * - 序列化时: 包含。
     * - 反序列化时: READ_ONLY。
     */
    @JsonProperty(value = "accountNonExpired", access = JsonProperty.Access.READ_ONLY)
    public abstract boolean isAccountNonExpired();

    /**
     * "accountNonLocked" 属性：
     * - 序列化时: 包含。
     * - 反序列化时: READ_ONLY。
     */
    @JsonProperty(value = "accountNonLocked", access = JsonProperty.Access.READ_ONLY)
    public abstract boolean isAccountNonLocked();

    /**
     * "credentialsNonExpired" 属性：
     * - 序列化时: 包含。
     * - 反序列化时: READ_ONLY。
     */
    @JsonProperty(value = "credentialsNonExpired", access = JsonProperty.Access.READ_ONLY)
    public abstract boolean isCredentialsNonExpired();

    /**
     * "enabled" 属性：
     * - 序列化时: 包含。
     * - 反序列化时: READ_ONLY。
     */
    @JsonProperty(value = "enabled", access = JsonProperty.Access.READ_ONLY)
    public abstract boolean isEnabled();
}