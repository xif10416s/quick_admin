package org.fxi.quick.module.activiti.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fxi.quick.common.model.PageSearchModel;

@Data
@EqualsAndHashCode
@ApiModel(value = "历史任务实例查询", description = "历史任务实例查询")
public class HistoryInstanceSearchModel extends PageSearchModel {

  @ApiModelProperty(value = "实例定义")
  private String definitionKey;

  @ApiModelProperty(value = "任务分类")
  private String category;


}
