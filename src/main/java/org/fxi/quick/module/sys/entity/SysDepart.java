package org.fxi.quick.module.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fxi.quick.common.entity.BaseEntity;

/**
 * <p>
 * 部门表
 * <p>
 *
 * @Author Steve
 * @Since 2019-01-22
 */
@Data
@TableName("sys_depart")
@EqualsAndHashCode
public class SysDepart extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;


  /**
   * 父机构ID
   */
  private Long parentId;
  /**
   * 机构/部门名称
   */
  private String departName;
  /**
   * 英文名
   */
  private String departNameEn;
  /**
   * 缩写
   */
  private String departNameAbbr;
  /**
   * 排序
   */
  private Integer departOrder;
  /**
   * 描述
   */
  private String description;
  /**
   * 机构类别 1组织机构，2岗位
   */
  private String orgCategory;
  /**
   * 机构类型
   */
  private String orgType;
  /**
   * 机构编码
   */
  private String orgCode;
  /**
   * 手机号
   */
  private String mobile;
  /**
   * 传真
   */
  private String fax;
  /**
   * 地址
   */
  private String address;
  /**
   * 备注
   */
  private String memo;
  /**
   * 状态（1启用，0不启用）
   */
  private Short status;

  /**
   * 创建人
   */
  private String createBy;

  /**
   * 更新人
   */
  private String updateBy;
}
