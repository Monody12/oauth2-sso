package org.example.sso.authorizationserver.service;

import lombok.RequiredArgsConstructor;
import org.example.sso.authorizationserver.dto.SysUserDto;
import org.example.sso.authorizationserver.entity.SysUserDo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final SysUserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserDo sysUser = userService.findByUsername(username);
        if (sysUser == null) {
            throw new RuntimeException("用户：" + username + "未注册");
        }
        // 后续自行修改和完善
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("/oauth2/token", "/oauth2/authorize", "/authorized");
        SysUserDto sysUserDto = new SysUserDto();
        sysUserDto.setUsername(username);
        sysUserDto.setNickname(sysUser.getNickname());
        sysUserDto.setDepartmentId(sysUser.getDepartmentId());
        sysUserDto.setAuthorities(authorityList);
        sysUserDto.setId(sysUser.getId());
        sysUserDto.setAvatar(sysUser.getAvatar());
        sysUserDto.setEmail(sysUser.getEmail());
        sysUserDto.setIdNumber(sysUser.getIdNumber());

        sysUserDto.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        sysUserDto.setStatus(sysUser.getStatus());
        sysUserDto.setPhone(sysUser.getPhone());
        return sysUserDto;
    }
}
