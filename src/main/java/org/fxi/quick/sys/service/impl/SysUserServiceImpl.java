package org.fxi.quick.sys.service.impl;

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
import org.fxi.quick.sys.convert.SysUserConverter;
import org.fxi.quick.sys.entity.SysPermission;
import org.fxi.quick.sys.entity.SysUser;
import org.fxi.quick.sys.entity.SysUserDepart;
import org.fxi.quick.sys.mapper.SysPermissionMapper;
import org.fxi.quick.sys.mapper.SysRoleMapper;
import org.fxi.quick.sys.mapper.SysUserMapper;
import org.fxi.quick.sys.mapper.SysUserRoleMapper;
import org.fxi.quick.sys.model.SysUserDepartModel;
import org.fxi.quick.sys.model.SysUserModel;
import org.fxi.quick.sys.model.SysUserSearchModel;
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
      throw new BizException("", "该用户不存在，请注册");
    }
    //情况2：根据用户信息查询，该用户已注销
    if (CommonConstant.DEL_FLAG_1.toString().equals(sysUser.getDelFlag())) {
      throw new BizException("", "该用户已注销");
    }
    //情况3：根据用户信息查询，该用户已冻结
    if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
      throw new BizException("", "该用户已冻结");
    }
  }

  @Override
  public Map<String, String> getDepNamesByUserIds(List<Long> userIds) {
    List<SysUserDepartModel> list = this.baseMapper.getDepNamesByUserIds(userIds);

    Map<String, String> res = new HashMap<String, String>();
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
}
