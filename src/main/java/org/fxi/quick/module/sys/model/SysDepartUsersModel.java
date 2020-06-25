package org.fxi.quick.module.sys.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class SysDepartUsersModel implements Serializable{
	private static final long serialVersionUID = 1L;

	/**部门id*/
	private String depId;
	/**对应的用户id集合*/
	private List<String> userIdList;
	public SysDepartUsersModel(String depId, List<String> userIdList) {
		super();
		this.depId = depId;
		this.userIdList = userIdList;
	}
    //update-begin--Author:kangxiaolin  Date:20190908 for：[512][部门管理]点击添加已有用户失败修复--------------------

	public SysDepartUsersModel(){

	}
    //update-begin--Author:kangxiaolin  Date:20190908 for：[512][部门管理]点击添加已有用户失败修复--------------------

}
