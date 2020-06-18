package org.fxi.quick.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.fxi.quick.module.sys.entity.SysPermission;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysPermissionService extends IService<SysPermission> {


	/**
	 *
	 * @param userId
	 * @return
	 */
	public List<SysPermission> queryByUserId(Long userId);
}
