package org.example.sso.authorizationserver.service;

import lombok.RequiredArgsConstructor;
import org.example.sso.authorizationserver.entity.RoleDo;
import org.example.sso.authorizationserver.entity.VwUserPermissions;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {
    private final PermissionService permissionService;

    public Set<GrantedAuthority> getAuthoritiesForUserInSystem(Integer userId, String systemCode) {
        // 这一步是关键，您需要创建一个 Mapper 方法来实现这个复杂的关联查询
        // 查询会连接 tb_user_role, tb_role, tb_system_role, tb_system
        List<VwUserPermissions> userPermissions = permissionService.getUserPermissions(userId, systemCode);

        // 从角色中提取权限 (permission_code)
        Set<GrantedAuthority> permissions = userPermissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionCode()))
                .collect(Collectors.toSet());

        // 从角色中提取角色编码 (role_code)，并添加 "ROLE_" 前缀
        Set<GrantedAuthority> roleAuthorities = userPermissions.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()))
                .collect(Collectors.toSet());

        // 合并两种权限
        return Stream.concat(permissions.stream(), roleAuthorities.stream()).collect(Collectors.toSet());
    }
}
