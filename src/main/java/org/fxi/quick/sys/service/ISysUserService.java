package org.fxi.quick.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Set;
import org.fxi.quick.sys.entity.SysUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {
	/**
	 * 通过用户名获取用户角色集合
	 *
	 * @param userId 用户Id
	 * @return 角色集合
	 */
	Set<String> getUserRolesSet(Long userId);

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param userId 用户Id
	 * @return 权限集合
	 */
	Set<String> getUserPermissionsSet(Long userId);


	/**
	 * 校验用户是否有效
	 * @param sysUser
	 * @return
	 */
	void checkUserIsEffective(SysUser sysUser);


}
