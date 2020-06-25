package org.fxi.quick.module.sys.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class SysUserRoleModel implements Serializable{
	private static final long serialVersionUID = 1L;

	/**部门id*/
	private Long roleId;
	/**对应的用户id集合*/
	private List<String> userIdList;

	public SysUserRoleModel() {
		super();
	}

	public SysUserRoleModel(Long roleId, List<String> userIdList) {
		super();
		this.roleId = roleId;
		this.userIdList = userIdList;
	}

}
