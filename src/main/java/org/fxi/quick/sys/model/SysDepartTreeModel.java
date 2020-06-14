package org.fxi.quick.sys.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fxi.quick.sys.entity.SysDepart;

/**
 * <p>
 * 部门表 存储树结构数据的实体类
 * <p>
 *
 * @Author Steve
 * @Since 2019-01-22
 */
@Data
@EqualsAndHashCode
public class SysDepartTreeModel implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 对应SysDepart中的id字段,前端数据树中的key
   */
  private Long key;

  /**
   * 对应SysDepart中的id字段,前端数据树中的value
   */
  private Long value;

  /**
   * 对应depart_name字段,前端数据树中的title
   */
  private String title;


  private boolean isLeaf;
  // 以下所有字段均与SysDepart相同

  private Long id;

  private Long parentId;

  private String departName;

  private String departNameEn;

  private String departNameAbbr;

  private Integer departOrder;

  private String description;

  private String orgCategory;

  private String orgType;

  private String orgCode;

  private String mobile;

  private String fax;

  private String address;

  private String memo;

  private Short status;

  private Short delFlag;

  private String createBy;

  private LocalDateTime createTime;

  private String updateBy;

  private LocalDateTime updateTime;

  private List<SysDepartTreeModel> children = new ArrayList<>();


  /**
   * 将SysDepart对象转换成SysDepartTreeModel对象
   */
  public SysDepartTreeModel(SysDepart sysDepart) {
    this.key = sysDepart.getId();
    this.value = sysDepart.getId();
    this.title = sysDepart.getDepartName();
    this.id = sysDepart.getId();
    this.parentId = sysDepart.getParentId();
    this.departName = sysDepart.getDepartName();
    this.departNameEn = sysDepart.getDepartNameEn();
    this.departNameAbbr = sysDepart.getDepartNameAbbr();
    this.departOrder = sysDepart.getDepartOrder();
    this.description = sysDepart.getDescription();
    this.orgCategory = sysDepart.getOrgCategory();
    this.orgType = sysDepart.getOrgType();
    this.orgCode = sysDepart.getOrgCode();
    this.mobile = sysDepart.getMobile();
    this.fax = sysDepart.getFax();
    this.address = sysDepart.getAddress();
    this.memo = sysDepart.getMemo();
    this.status = sysDepart.getStatus();
    this.delFlag = sysDepart.getDelFlag();
    this.createBy = sysDepart.getCreateBy();
    this.createTime = sysDepart.getCreateTime();
    this.updateBy = sysDepart.getUpdateBy();
    this.updateTime = sysDepart.getUpdateTime();
  }


  public List<SysDepartTreeModel> getChildren() {
    return children;
  }

  public void setChildren(List<SysDepartTreeModel> children) {
    if (children == null) {
      this.isLeaf = true;
    }
    this.children = children;
  }

  public SysDepartTreeModel() {
  }

}
