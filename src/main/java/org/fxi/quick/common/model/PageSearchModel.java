package org.fxi.quick.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageSearchModel {
  @ApiModelProperty(value = "分页页码")
  private int pageNo = 1;

  @ApiModelProperty(value = "每页数量")
  private int pageSize = 10;

  @ApiModelProperty(value = "排序字段")
  private String column;

  @ApiModelProperty(value = "顺序")
  private String order;
}
