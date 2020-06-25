package org.fxi.quick.module.activiti.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fxi.quick.common.model.PageSearchModel;

@Data
@EqualsAndHashCode
@ApiModel(value = "工作流实例查询模型", description = "工作流实例查询模型")
public class ProcessInstSearchModel extends PageSearchModel {

  @ApiModelProperty(value = "工作流分类")
  private String category;

  @ApiModelProperty(value = "发起人")
  String involvedUser;

  @ApiModelProperty(value = "是否暂停：1激活，2暂停")
  Integer suspended;
}
