package org.fxi.quick.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Arrays;
import org.fxi.quick.module.sys.entity.SysRole;
import org.fxi.quick.module.sys.entity.SysUser;
import org.fxi.quick.module.sys.entity.SysUserDepart;
import org.fxi.quick.module.sys.entity.SysUserRole;
import org.fxi.quick.module.sys.mapper.SysRoleMapper;
import org.fxi.quick.module.sys.mapper.SysUserDepartMapper;
import org.fxi.quick.module.sys.mapper.SysUserMapper;
import org.fxi.quick.module.sys.mapper.SysUserRoleMapper;
import org.fxi.quick.module.sys.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements
    ISysRoleService {
    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

//    @Override
//    public Result importExcelCheckRoleCode(MultipartFile file, ImportParams params) throws Exception {
//        List<Object> listSysRoles = ExcelImportUtil.importExcel(file.getInputStream(), SysRole.class, params);
//        int totalCount = listSysRoles.size();
//        List<String> errorStrs = new ArrayList<>();
//
//        // 去除 listSysRoles 中重复的数据
//        for (int i = 0; i < listSysRoles.size(); i++) {
//            String roleCodeI =((SysRole)listSysRoles.get(i)).getRoleCode();
//            for (int j = i + 1; j < listSysRoles.size(); j++) {
//                String roleCodeJ =((SysRole)listSysRoles.get(j)).getRoleCode();
//                // 发现重复数据
//                if (roleCodeI.equals(roleCodeJ)) {
//                    errorStrs.add("第 " + (j + 1) + " 行的 roleCode 值：" + roleCodeI + " 已存在，忽略导入");
//                    listSysRoles.remove(j);
//                    break;
//                }
//            }
//        }
//        // 去掉 sql 中的重复数据
//        Integer errorLines=0;
//        Integer successLines=0;
//        List<String> list = ImportExcelUtil.importDateSave(listSysRoles, ISysRoleService.class, errorStrs, CommonConstant.SQL_INDEX_UNIQ_SYS_ROLE_CODE);
//         errorLines+=list.size();
//         successLines+=(listSysRoles.size()-errorLines);
//        return ImportExcelUtil.imporReturnRes(errorLines,successLines,list);
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(String roleid) {
        //1.删除角色和用户关系
        sysRoleMapper.deleteRoleUserRelation(roleid);
        //2.删除角色和权限关系
        sysRoleMapper.deleteRolePermissionRelation(roleid);
        //3.删除角色
        this.removeById(roleid);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchRole(String[] roleIds) {
        //1.删除角色和用户关系
        sysUserMapper.deleteBathRoleUserRelation(roleIds);
        //2.删除角色和权限关系
        sysUserMapper.deleteBathRolePermissionRelation(roleIds);
        //3.删除角色
        this.removeByIds(Arrays.asList(roleIds));
        return true;
    }

    @Override
    @Transactional
    public void saveUserWithDepart(SysUser user, String selectedParts) {
//		this.save(user);  //保存角色的时候已经添加过一次了
        if(StringUtils.isNotBlank(selectedParts)) {
            String[] arr = selectedParts.split(",");
            for (String departId : arr) {
                SysUserDepart userDept = new SysUserDepart(user.getId(), Long.valueOf(departId));
                sysUserDepartMapper.insert(userDept);
            }
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void saveNewUser(SysUser user, String roles, String selectedParts) {
        saveUserWithRole(user,roles);
        saveUserWithDepart(user,selectedParts);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void editNewUser(SysUser user, String roles, String selectedParts) {
        sysUserMapper.updateById(user);
        //先删后加
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
        saveRole(user,roles);
        editUserWithDepart(user,selectedParts);
    }

    /**
     * 编辑角色
     * @param user
     * @param roles
     */
    private void saveRole(SysUser user, String roles){
        if(StringUtils.isNotBlank(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                SysUserRole userRole = new SysUserRole(user.getId(), Long.valueOf(roleId));
                sysUserRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void saveUserWithRole(SysUser user, String roles) {
        sysUserMapper.insert(user);
        saveRole(user, roles);
    }

    private void editUserWithDepart(SysUser user, String departs) {
        String[] arr = {};
        if(StringUtils.isNotBlank(departs)){
            arr = departs.split(",");
        }
        //先删后加
        sysUserDepartMapper.delete(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
        if(StringUtils.isNotBlank(departs)) {
            for (String departId : arr) {
                SysUserDepart userDepart = new SysUserDepart(user.getId(), Long.valueOf(departId));
                sysUserDepartMapper.insert(userDepart);
            }
        }
    }

}
