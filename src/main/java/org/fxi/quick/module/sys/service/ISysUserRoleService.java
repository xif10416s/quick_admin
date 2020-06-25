package org.fxi.quick.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.fxi.quick.module.sys.entity.SysUserRole;
import org.fxi.quick.module.sys.model.SysRoleModel;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

  /**
   * 获取用户角色信息
   * @param userId
   * @return
   */
  List<SysRoleModel> getUserRole(Long userId);
}
