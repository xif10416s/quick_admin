package org.fxi.quick.sys.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@ApiModel(value = "部门信息", description = "部门信息")
public class SysDepartModel implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * ID
   */
  @ApiModelProperty(value = "部门id")
  private Long id;

  /**
   * 父机构ID
   */
  @ApiModelProperty(value = "从属部门id/父机构ID")
  private Long parentId;
  /**
   * 机构/部门名称
   */
  @ApiModelProperty(value = "机构/部门名称")
  private String departName;
  /**
   * 英文名
   */
  @ApiModelProperty(value = "机构/部门英文名")
  private String departNameEn;
  /**
   * 缩写
   */
  @ApiModelProperty(value = "机构/部门缩写")
  private String departNameAbbr;
  /**
   * 排序
   */
  @ApiModelProperty(value = "排序")
  private Integer departOrder;
  /**
   * 描述
   */
  @ApiModelProperty(value = "描述")
  private String description;
  /**
   * 机构类别 1组织机构，2岗位
   */
  @ApiModelProperty(value = "机构类别 1组织机构，2岗位")
  private String orgCategory;
  /**
   * 机构类型
   */
  @ApiModelProperty(value = "机构类型")
  private String orgType;
  /**
   * 机构编码
   */
  @ApiModelProperty(value = "机构编码")
  private String orgCode;
  /**
   * 手机号
   */
  @ApiModelProperty(value = "手机号")
  private String mobile;
  /**
   * 传真
   */
  @ApiModelProperty(value = "传真")
  private String fax;
  /**
   * 地址
   */
  @ApiModelProperty(value = "地址")
  private String address;
  /**
   * 备注
   */
  @ApiModelProperty(value = "备注")
  private String memo;
  /**
   * 状态（1启用，0不启用）
   */
  @ApiModelProperty(value = "状态（1启用，0不启用）")
  private Short status;

  /**
   * 创建人
   */
  @ApiModelProperty(value = "创建人")
  private String createBy;

  /**
   * 更新人
   */
  @ApiModelProperty(value = "更新人")
  private String updateBy;
}
