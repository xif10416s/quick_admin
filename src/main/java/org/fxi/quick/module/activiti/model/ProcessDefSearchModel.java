package org.fxi.quick.module.activiti.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fxi.quick.common.model.PageSearchModel;

@Data
@EqualsAndHashCode
@ApiModel(value = "工作流定义查询模型", description = "工作流定义查询模型")
public class ProcessDefSearchModel extends PageSearchModel {

  @ApiModelProperty(value = "工作流分类")
  private String category;

  @ApiModelProperty(value = "工作流名称")
  String name;

  @ApiModelProperty(value = "标识")
  String key;

  @ApiModelProperty(value = "是否暂停：1激活，2暂停")
  Integer suspended;
}
