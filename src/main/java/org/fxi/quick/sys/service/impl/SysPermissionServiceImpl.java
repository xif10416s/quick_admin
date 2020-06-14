package org.fxi.quick.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.fxi.quick.sys.entity.SysPermission;
import org.fxi.quick.sys.mapper.SysPermissionMapper;
import org.fxi.quick.sys.service.ISysPermissionService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysPermissionServiceImpl  extends
    ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

  @Override
  public List<SysPermission> queryByUserId(Long userId) {
    List<SysPermission> permissionList = this.getBaseMapper().queryByUserId(userId);
    return permissionList;
  }
}
