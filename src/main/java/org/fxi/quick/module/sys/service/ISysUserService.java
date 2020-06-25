package org.fxi.quick.module.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.fxi.quick.module.sys.model.SysUserModel;
import org.fxi.quick.module.sys.model.SysUserSearchModel;
import org.fxi.quick.module.sys.entity.SysUser;
import org.fxi.quick.module.sys.model.SysUserSysDepartModel;

/**
 * <p>
 * 用户表 服务类
 * </p>
 * TODO 这个类@Transaction 不生效
 * @Author scott
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {

	/**
	 *
	 * @param searchModel
	 * @return
	 */
	IPage<SysUserModel> pageSearch(SysUserSearchModel searchModel);
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

	/**
	 *
	 * @param userIds
	 * @return
	 */
	 Map<Long, String> getDepNamesByUserIds(List<Long> userIds);


	/**
	 * 添加用户和用户角色关系
	 * @param user
	 * @param roles
	 */
	 void saveUserWithRole(SysUser user,String roles);

	/**
	 * 添加用户和用户部门关系
	 * @param user
	 * @param selectedParts
	 */
	void saveUserWithDepart(SysUser user, String selectedParts);

	/**
	 *
	 * 添加用户
	 * @param user
	 * @param roles
	 */
	 void saveNewUser(SysUser user,String roles, String selectedParts);

	/**
	 * 修改密码
	 *
	 * @param sysUser
	 * @return
	 */
	 void changePassword(SysUser sysUser);

	/**
	 * 根据角色Id查询
	 * @param
	 * @return
	 */
	 IPage<SysUserModel> getUserByRoleId(Page<SysUser> page,Long roleId, String username);

	/**
	 * 根据用户名设置部门ID
	 * @param username
	 * @param orgCode
	 */
	void updateUserDepart(String username,String orgCode);

	/**
	 *
	 * @param username
	 * @return
	 */
	SysUser getUserByName(String username);

	/**
	 * 根据部门Ids查询
	 * @param
	 * @return
	 */
	 IPage<SysUser> getUserByDepIds(Page<SysUser> page, List<String> departIds, String username);


	/**
	 * 根据 orgCode 查询用户，包括子部门下的用户
	 *
	 * @param orgCode
	 * @param userParams 用户查询条件，可为空
	 * @param page 分页参数
	 * @return
	 */
	IPage<SysUserSysDepartModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage page);


}
