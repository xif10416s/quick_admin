package org.fxi.quick.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.fxi.quick.common.exception.BizException;
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
	 List<SysPermission> queryByUserId(Long userId);

	/**
	 *
	 * @param sysPermission
	 * @throws BizException
	 */
	 void addPermission(SysPermission sysPermission) throws BizException;

	 void editPermission(SysPermission sysPermission) throws BizException;

	/**真实删除*/
	void deletePermission(Long id) throws BizException;

}
