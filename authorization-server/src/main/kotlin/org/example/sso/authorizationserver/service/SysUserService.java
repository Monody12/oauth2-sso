package org.example.sso.authorizationserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.sso.authorizationserver.entity.SysUserDo;
import org.example.sso.authorizationserver.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUserDo> implements IService<SysUserDo> {

    public SysUserDo findByUsername(String username) {
        var wrapper = Wrappers.lambdaQuery(SysUserDo.class);
        wrapper.eq(SysUserDo::getUsername, username);
        return this.getOne(wrapper);
    }

}
