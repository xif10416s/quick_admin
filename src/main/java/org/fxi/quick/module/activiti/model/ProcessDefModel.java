package org.fxi.quick.module.activiti.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@ApiModel(value = "工作流定义模型", description = "工作流定义模型")
public class ProcessDefModel {
  @ApiModelProperty(value = "工作流id")
  private String id;

  @ApiModelProperty(value = "工作流分类")
  private String category;

  @ApiModelProperty(value = "工作流名称")
  String name;

  @ApiModelProperty(value = "标识")
  String key;

  @ApiModelProperty(value = "描述")
  String description;

  @ApiModelProperty(value = "资源名称")
  String resourceName;

  @ApiModelProperty(value = "部署id")
  String deploymentId;

  @ApiModelProperty(value = "资源图片名称")
  String diagramResourceName;

  @ApiModelProperty(value = "是否暂停：1激活，2暂停")
  boolean suspended;
}
