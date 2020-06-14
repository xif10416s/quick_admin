package org.fxi.quick.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.fxi.quick.sys.entity.SysUser;
import org.fxi.quick.sys.entity.SysUserDepart;
import org.fxi.quick.sys.model.DepartIdModel;

/**
 * <p>
 * SysUserDpeart用户组织机构service
 * </p>
 * @Author ZhiLin
 *
 */
public interface ISysUserDepartService extends IService<SysUserDepart> {


	/**
	 * 根据指定用户id查询部门信息
	 * @param userId
	 * @return
	 */
	List<DepartIdModel> queryDepartIdsOfUser(Long userId);


	/**
	 * 根据部门id查询用户信息
	 * @param depId
	 * @return
	 */
	List<SysUser> queryUserByDepId(Long depId);
  	/**
	 * 根据部门code，查询当前部门和下级部门的用户信息
	 */
	public List<SysUser> queryUserByDepCode(String depCode, String realname);
}
