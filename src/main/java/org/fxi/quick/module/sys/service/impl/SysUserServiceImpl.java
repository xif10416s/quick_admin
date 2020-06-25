package org.fxi.quick.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.fxi.quick.common.constant.CommonConstant;
import org.fxi.quick.common.exception.BizException;
import org.fxi.quick.common.util.PasswordUtil;
import org.fxi.quick.module.sys.convert.SysUserConverter;
import org.fxi.quick.module.sys.entity.SysPermission;
import org.fxi.quick.module.sys.entity.SysUser;
import org.fxi.quick.module.sys.entity.SysUserDepart;
import org.fxi.quick.module.sys.entity.SysUserRole;
import org.fxi.quick.module.sys.mapper.SysPermissionMapper;
import org.fxi.quick.module.sys.mapper.SysUserDepartMapper;
import org.fxi.quick.module.sys.mapper.SysUserMapper;
import org.fxi.quick.module.sys.mapper.SysUserRoleMapper;
import org.fxi.quick.module.sys.model.SysUserDepartModel;
import org.fxi.quick.module.sys.model.SysUserModel;
import org.fxi.quick.module.sys.model.SysUserSearchModel;
import org.fxi.quick.module.sys.model.SysUserSysDepartModel;
import org.fxi.quick.module.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
  private SysPermissionMapper sysPermissionMapper;
  @Autowired
  private SysUserRoleMapper sysUserRoleMapper;
  @Autowired
  private SysUserDepartMapper sysUserDepartMapper;


  @Override
  public IPage<SysUserModel> pageSearch(SysUserSearchModel searchModel) {
    Page<SysUser> page = new Page<SysUser>(searchModel.getPageNo(), searchModel.getPageSize());
    LambdaQueryWrapper<SysUser> lambda = new QueryWrapper<SysUser>().lambda();
    if(StringUtils.isNotBlank(searchModel.getUsername())){
      lambda.like(SysUser::getUsername,searchModel.getUsername());
    }

    if(StringUtils.isNotBlank(searchModel.getPhone())){
      lambda.eq(SysUser::getPhone,searchModel.getPhone());
    }

    if(searchModel.getStatus() != null){
      lambda.eq(SysUser::getStatus,searchModel.getStatus());
    }

    if(searchModel.getSex() != null){
      lambda.eq(SysUser::getSex,searchModel.getSex());
    }

    if(StringUtils.isNotBlank(searchModel.getRealname())){
      lambda.like(SysUser::getRealname,searchModel.getRealname());
    }

    return  this.page(page, lambda).convert(SysUserConverter.INSTANCE::convertToModel);

  }

  @Override
  public Set<String> getUserRolesSet(Long userId) {
    List<String> roles = sysUserRoleMapper.getRoleByUserId(userId);
    return new HashSet<>(roles);
  }

  /**
   * 权限配置有url的用url , 没有的在perms中指定
   * @param userId 用户Id
   * @return
   */
  @Override
  public Set<String> getUserPermissionsSet(Long userId) {
    Set<String> permissionSet = new HashSet<>();
    List<SysPermission> permissionList = sysPermissionMapper.queryByUserId(userId);
    for (SysPermission po : permissionList) {
      if (StringUtils.isNotBlank(po.getPerms())) {
        permissionSet.add(po.getPerms());
      }
      if (StringUtils.isNotBlank(po.getUrl())) {
        permissionSet.add(po.getUrl());
      }
    }
    return permissionSet;
  }

  @Override
  public void checkUserIsEffective(SysUser sysUser) {
    if (sysUser == null) {
      throw new BizException("A0201");
    }
    //情况2：根据用户信息查询，该用户已注销
    if (CommonConstant.DEL_FLAG_1.toString().equals(sysUser.getDelFlag())) {
      throw new BizException("A0203");
    }
    //情况3：根据用户信息查询，该用户已冻结
    if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
      throw new BizException("A0202");
    }
  }

  @Override
  public Map<Long, String> getDepNamesByUserIds(List<Long> userIds) {
    List<SysUserDepartModel> list = this.baseMapper.getDepNamesByUserIds(userIds);

    Map<Long, String> res = new HashMap<Long, String>(16);
    list.forEach(item -> {
          if (res.get(item.getUserId()) == null) {
            res.put(item.getUserId(), item.getDepartName());
          } else {
            res.put(item.getUserId(), res.get(item.getUserId()) + "," + item.getDepartName());
          }
        }
    );
    return res;
  }


  @Override
  @Transactional
  public void saveUserWithDepart(SysUser user, String selectedParts) {
//		this.save(user);  //保存角色的时候已经添加过一次了
    if(StringUtils.isNotBlank(selectedParts)) {
      String[] arr = selectedParts.split(",");
      for (String departId : arr) {
        SysUserDepart userDept = new SysUserDepart(user.getId(), Long.valueOf(departId));
        sysUserDepartMapper.insert(userDept);
      }
    }
  }

  @Override
  public void saveNewUser(SysUser user, String roles, String selectedParts) {
    saveUserWithRole(user,roles);
    saveUserWithDepart(user,selectedParts);
  }

  @Override
  @Transactional(rollbackFor=Exception.class)
  public void saveUserWithRole(SysUser user, String roles) {
    this.baseMapper.insert(user);
    if(StringUtils.isNotBlank(roles)) {
      String[] arr = roles.split(",");
      for (String roleId : arr) {
        SysUserRole userRole = new SysUserRole(user.getId(), Long.valueOf(roleId));
        sysUserRoleMapper.insert(userRole);
      }
    }
  }

  @Override
  public void changePassword(SysUser sysUser) {
    String salt = PasswordUtil.randomGen(8);
    sysUser.setSalt(salt);
    String password = sysUser.getPassword();
    String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
    sysUser.setPassword(passwordEncode);
    this.getBaseMapper().updateById(sysUser);
  }

  @Override
  public IPage<SysUserModel> getUserByRoleId(Page<SysUser> page, Long roleId, String username) {
    return this.getBaseMapper().getUserByRoleId(page,roleId,username)
        .convert(SysUserConverter.INSTANCE::convertToModel);
  }

  @Override
  public void updateUserDepart(String username, String orgCode) {
    baseMapper.updateUserDepart(username, orgCode);
  }

  @Override
  public SysUser getUserByName(String username) {
    return baseMapper.getUserByName(username);
  }

  @Override
  public IPage<SysUser> getUserByDepIds(Page<SysUser> page, List<String> departIds, String username) {
    return baseMapper.getUserByDepIds(page, departIds,username);
  }

  @Override
  public IPage<SysUserSysDepartModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage page) {
    List<SysUserSysDepartModel> list = baseMapper.getUserByOrgCode(page, orgCode, userParams);
    Integer total = baseMapper.getUserByOrgCodeTotal(orgCode, userParams);

    IPage<SysUserSysDepartModel> result = new Page<>(page.getCurrent(), page.getSize(), total);
    result.setRecords(list);

    return result;
  }
}
