package org.fxi.quick.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.fxi.quick.module.sys.entity.SysRolePermission;
import org.fxi.quick.module.sys.mapper.SysRolePermissionMapper;
import org.fxi.quick.module.sys.service.ISysRolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Service
public class SysRolePermissionServiceImpl extends
    ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

	@Override
	public void saveRolePermission(Long roleId, String permissionIds) {
		LambdaQueryWrapper<SysRolePermission> query = new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId);
		this.remove(query);
		List<SysRolePermission> list = new ArrayList<>();
        String[] arr = permissionIds.split(",");
		for (String p : arr) {
			if(StringUtils.isNotBlank(p)) {
				SysRolePermission rolepms = new SysRolePermission(roleId, Long.valueOf(p));
				list.add(rolepms);
			}
		}
		this.saveBatch(list);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveRolePermission(Long roleId, String permissionIds, String lastPermissionIds) {
		// 全删
		this.remove(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId));
		// 全插
		List<SysRolePermission> list = new ArrayList<SysRolePermission>();
		for (String p : permissionIds.split(",")) {
			if(StringUtils.isNotBlank(p)) {
				SysRolePermission rolepms = new SysRolePermission(roleId,Long.valueOf( p));
				list.add(rolepms);
			}
		}
		this.saveBatch(list);
	}

	/**
	 * 从diff中找出main中没有的元素
	 * @param main
	 * @param diff
	 * @return
	 */
	private List<String> getDiff(String main,String diff){
		if(StringUtils.isBlank(diff)) {
			return null;
		}
		if(StringUtils.isNotBlank(main)) {
			return Arrays.asList(diff.split(","));
		}

		String[] mainArr = main.split(",");
		String[] diffArr = diff.split(",");
		Map<String, Integer> map = new HashMap<>();
		for (String string : mainArr) {
			map.put(string, 1);
		}
		List<String> res = new ArrayList<String>();
		for (String key : diffArr) {
			if(StringUtils.isNotBlank(key) && !map.containsKey(key)) {
				res.add(key);
			}
		}
		return res;
	}

}
