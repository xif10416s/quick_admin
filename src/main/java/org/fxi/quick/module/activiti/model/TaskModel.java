package org.fxi.quick.module.activiti.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fxi.quick.common.constant.DateFormat;

@Data
@EqualsAndHashCode
@ApiModel(value = "任务模型", description = "任务模型")
public class TaskModel {

  @ApiModelProperty(value = "任务id")
  private String id;


  @ApiModelProperty(value = "任务名称")
  private String name;

  @ApiModelProperty(value = "任务描述")
  private String description;

  @ApiModelProperty(value = "任务优先级")
  private int priority;

  @ApiModelProperty(value = "任务负责人")
  private String owner;

  @ApiModelProperty(value = "任务处理人")
  private String assignee;

  @ApiModelProperty(value = "任务截至日期")
  @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
  private LocalDateTime dueDate;

  @ApiModelProperty(value = "任务创建日期")
  @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
  private LocalDateTime createTime;

  @ApiModelProperty(value = "任务分类")
  private String category;

  @ApiModelProperty(value = "任务状态 是否暂停：1激活，2暂停")
  private String suspended;

  @ApiModelProperty(value = "任务定义key")
  private String taskDefinitionKey;

  @ApiModelProperty(value = "流程实例id")
  private String processInstanceId;
}
