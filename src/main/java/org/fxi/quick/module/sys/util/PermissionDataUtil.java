package org.fxi.quick.module.sys.util;

import java.util.List;
import org.fxi.quick.module.sys.entity.SysPermission;

/**
 * @Author: scott
 * @Date: 2019-04-03
 */
public class PermissionDataUtil {


	/**
	 * 判断是否授权首页
	 * @param metaList
	 * @return
	 */
	public static boolean hasIndexPage(List<SysPermission> metaList){
		boolean hasIndexMenu = false;
		for (SysPermission sysPermission : metaList) {
			if("首页".equals(sysPermission.getName())) {
				hasIndexMenu = true;
				break;
			}
		}
		return hasIndexMenu;
	}

}
