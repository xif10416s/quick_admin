package org.fxi.quick.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
import org.fxi.quick.module.sys.convert.SysRoleConverter;
import org.fxi.quick.module.sys.entity.SysRole;
import org.fxi.quick.module.sys.entity.SysUserRole;
import org.fxi.quick.module.sys.mapper.SysRoleMapper;
import org.fxi.quick.module.sys.mapper.SysUserRoleMapper;
import org.fxi.quick.module.sys.model.SysRoleModel;
import org.fxi.quick.module.sys.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements
    ISysUserRoleService {

  @Autowired
  private SysRoleMapper sysRoleMapper;

  @Override
  public List<SysRoleModel> getUserRole(Long userId) {
    List<Long> userRoleList = this.getBaseMapper()
        .selectList(new QueryWrapper<SysUserRole>().lambda()
            .select(SysUserRole::getRoleId)
            .eq(SysUserRole::getUserId, userId)).stream()
        .map(item -> item.getRoleId()).collect(Collectors.toList());

    List<SysRole> sysRoles = sysRoleMapper.selectList(new QueryWrapper<SysRole>().lambda()
        .in(SysRole::getId, userRoleList));

    return SysRoleConverter.INSTANCE.convertToModel(sysRoles);
  }
}
