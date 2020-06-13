package org.fxi.quick.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fxi.quick.common.constant.CommonConstant;
import org.fxi.quick.common.exception.BizException;
import org.fxi.quick.sys.entity.SysPermission;
import org.fxi.quick.sys.entity.SysUser;
import org.fxi.quick.sys.mapper.SysPermissionMapper;
import org.fxi.quick.sys.mapper.SysRoleMapper;
import org.fxi.quick.sys.mapper.SysUserMapper;
import org.fxi.quick.sys.mapper.SysUserRoleMapper;
import org.fxi.quick.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @Author: scott
 * @Date: 2018-12-20
 */
@Service
@Slf4j
        public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements
    ISysUserService {

  @Autowired
  private SysUserMapper userMapper;
  @Autowired
  private SysPermissionMapper sysPermissionMapper;
  @Autowired
  private SysUserRoleMapper sysUserRoleMapper;
  @Autowired
  private SysRoleMapper sysRoleMapper;


  @Override
  public Set<String> getUserRolesSet(Long userId) {
    List<String> roles = sysUserRoleMapper.getRoleByUserId(userId);
    return new HashSet<>(roles);
  }

  @Override
  public Set<String> getUserPermissionsSet(Long userId) {
    Set<String> permissionSet = new HashSet<>();
    List<SysPermission> permissionList = sysPermissionMapper.queryByUserId(userId);
    for (SysPermission po : permissionList) {
      if (StringUtils.isNotEmpty(po.getPerms())) {
        permissionSet.add(po.getPerms());
      }
    }
    return permissionSet;
  }

  @Override
  public void checkUserIsEffective(SysUser sysUser) {
    if (sysUser == null) {
      throw new BizException("","该用户不存在，请注册");
    }
    //情况2：根据用户信息查询，该用户已注销
    if (CommonConstant.DEL_FLAG_1.toString().equals(sysUser.getDelFlag())) {
      throw new BizException("","该用户已注销");
    }
    //情况3：根据用户信息查询，该用户已冻结
    if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
      throw new BizException("","该用户已冻结");
    }
  }


}
