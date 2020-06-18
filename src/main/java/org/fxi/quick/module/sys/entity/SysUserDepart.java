package org.fxi.quick.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

@Data
@TableName("sys_user_depart")
public class SysUserDepart implements Serializable {
	private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.AUTO)
	private Long id;
	/**用户id*/
	private Long userId;
	/**部门id*/
	private Long depId;


	public SysUserDepart(Long userId, Long departId) {
		this.userId = userId;
		this.depId = departId;
	}
}
