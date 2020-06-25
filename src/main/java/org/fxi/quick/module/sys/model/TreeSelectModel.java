package org.fxi.quick.module.sys.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
  * 树形下拉框
 */
@Data
public class TreeSelectModel implements Serializable {

	private static final long serialVersionUID = 9016390975325574747L;

	private Long key;

	private String title;

	private boolean isLeaf;

	private String icon;

	private String parentId;

	private String value;

	private String code;

	private List<TreeSelectModel> children;

}
