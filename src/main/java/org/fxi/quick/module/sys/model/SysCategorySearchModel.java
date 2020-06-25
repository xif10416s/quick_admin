package org.fxi.quick.module.sys.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.fxi.quick.common.model.PageSearchModel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 分类字典
 * @Author: jeecg-boot
 * @Date:   2019-05-29
 * @Version: V1.0
 */
@Data
public class SysCategorySearchModel extends PageSearchModel implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
	private Long id;
	/**父级节点*/
	private Long pid;
	/**类型名称*/
	private String name;
	/**类型编码*/
	private String code;
	/**创建人*/
	private String createBy;
	/**更新人*/
	private String updateBy;

	/**所属部门*/
	private String sysOrgCode;
	/**是否有子节点*/
	private Short hasChild;
}
