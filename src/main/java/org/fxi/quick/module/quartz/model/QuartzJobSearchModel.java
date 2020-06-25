package org.fxi.quick.module.quartz.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fxi.quick.common.model.PageSearchModel;

@Data
@EqualsAndHashCode
@ApiModel(value = "定时任务检索条件对象", description = "定时任务检索条件对象")
public class QuartzJobSearchModel extends PageSearchModel {
  /**
   * 任务类名
   */
  @ApiModelProperty(value="任务类名")
  private String jobClassName;

  /**
   * 任务类名
   */
  @ApiModelProperty(value="任务状态")
  private Short status;
}
