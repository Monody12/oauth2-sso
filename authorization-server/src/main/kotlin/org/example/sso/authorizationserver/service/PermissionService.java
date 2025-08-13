package org.example.sso.authorizationserver.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.example.sso.authorizationserver.entity.VwUserPermissions;
import org.example.sso.authorizationserver.mapper.VwUserPermissionsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final VwUserPermissionsMapper vwUserPermissionsMapper;

    /**
     * 根据用户ID获取该用户的所有权限信息
     *
     * @param userId 用户ID
     * @param systemCode 系统编号
     * @return 权限列表
     */
    public List<VwUserPermissions> getUserPermissions(Integer userId, String systemCode) {
        // 1. 创建查询条件构造器
        LambdaQueryWrapper<VwUserPermissions> queryWrapper = Wrappers.lambdaQuery();

        // 2. 添加查询条件：WHERE user_id = ?
        queryWrapper.eq(VwUserPermissions::getUserId, userId);

        // 你还可以添加其他条件，比如指定某个系统
        queryWrapper.eq(systemCode != null, VwUserPermissions::getSystemCode, systemCode);

        // 3. 执行查询
        return vwUserPermissionsMapper.selectList(queryWrapper);
    }

}
