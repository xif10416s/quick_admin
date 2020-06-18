package org.fxi.quick.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.fxi.quick.module.sys.entity.SysRole;
import org.fxi.quick.module.sys.entity.SysUser;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 删除角色
     * @param roleid
     * @return
     */
    public boolean deleteRole(String roleid);

    /**
     * 批量删除角色
     * @param roleids
     * @return
     */
     boolean deleteBatchRole(String[] roleids);

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
     * 添加用户
     * @param user
     * @param roles
     */
    void saveNewUser(SysUser user,String roles, String selectedParts);

    /**
     * 编辑用户
     * @param user
     * @param roles
     */
    void editNewUser(SysUser user,String roles, String selectedParts);

}
