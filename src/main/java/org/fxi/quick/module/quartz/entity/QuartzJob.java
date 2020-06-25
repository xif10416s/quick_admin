package org.fxi.quick.module.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import org.fxi.quick.common.annotation.Dict;
import org.fxi.quick.common.entity.BaseEntity;

/**
 * @Description: 定时任务在线管理
 * @Author: jeecg-boot
 * @Date:  2019-01-02
 * @Version: V1.0
 */
@Data
@TableName("sys_quartz_job")
public class QuartzJob extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;


	/**创建人*/
	private String createBy;

	/**修改人*/
	private String updateBy;

	/**任务类名*/
	private String jobClassName;
	/**cron表达式*/
	private String cronExpression;
	/**参数*/
	private String parameter;
	/**描述*/
	private String description;
	/**状态 0正常 -1停止*/
	@Dict(dicCode = "quartz_status")
	private Integer status;

}
