package org.fxi.quick.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.fxi.quick.sys.entity.SysPermission;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
//	/**
//	   * 通过父菜单ID查询子菜单
//	 * @param parentId
//	 * @return
//	 */
//	public List<TreeModel> queryListByParentId(@Param("parentId") String parentId);
//
	/**
	  *   根据用户查询用户权限
	 */
	@Select("select p.* from sys_user_role r join sys_role_permission rp on r.role_id = rp.role_id join sys_permission p on p.id = rp.permission_id where r.user_id = #{userId} and p.del_flag = 0  ")
	public List<SysPermission> queryByUserId(@Param("userId") Long userId);


}
