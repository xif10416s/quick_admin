package org.fxi.quick.module.activiti.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fxi.quick.common.model.PageSearchModel;

/**
 *
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "流程实例", description = "流程实例")
public class ProcessInstanceModel extends PageSearchModel {
  @ApiModelProperty(value = "业务号")
  private String businessKey;

  @ApiModelProperty(value = "流程名称")
  private String name;


  @ApiModelProperty(value = "发起人")
  private String startUserId;


  @ApiModelProperty(value = "开始时间")
  private LocalDateTime startTime;

  @ApiModelProperty(value = "流程定义id")
  String processDefinitionId;

  @ApiModelProperty(value = "流程id")
  String processInstanceId;

  @ApiModelProperty(value = "流程分类")
  private String category;

  @ApiModelProperty(value = "是否暂停：1激活，2暂停")
  String suspended;
}
