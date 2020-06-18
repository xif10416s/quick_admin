package org.fxi.quick.module.sys.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.fxi.quick.module.sys.entity.SysPermission;


/**
  * 树形列表用到
 */
@Data
public class TreeModel implements Serializable {

	private static final long serialVersionUID = 4013193970046502756L;

	private Long key;

	private String title;

	private String slotTitle;

	private boolean isLeaf;

	private String icon;

	private Integer ruleFlag;

	private Map<String,String> scopedSlots;

	public Map<String, String> getScopedSlots() {
		return scopedSlots;
	}

	public void setScopedSlots(Map<String, String> scopedSlots) {
		this.scopedSlots = scopedSlots;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	private List<TreeModel> children;

	public List<TreeModel> getChildren() {
		return children;
	}

	public void setChildren(List<TreeModel> children) {
		this.children = children;
	}

	public TreeModel() {

	}

	public TreeModel(SysPermission permission) {
		this.key = permission.getId();
		this.icon = permission.getIcon();
		this.parentId = permission.getParentId();
		this.title = permission.getName();
		this.slotTitle =  permission.getName();
		this.value = permission.getId();
		this.isLeaf = permission.isLeaf();
		this.label = permission.getName();
		if(!permission.isLeaf()) {
			this.children = new ArrayList<TreeModel>();
		}
	}

	 public TreeModel(Long key,Long parentId,String slotTitle,Integer ruleFlag,boolean isLeaf) {
    	this.key = key;
    	this.parentId = parentId;
    	this.ruleFlag=ruleFlag;
    	this.slotTitle =  slotTitle;
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("title", "hasDatarule");
    	this.scopedSlots = map;
    	this.isLeaf = isLeaf;
    	this.value = key;
    	if(!isLeaf) {
    		this.children = new ArrayList<TreeModel>();
    	}
    }

	 private Long parentId;

	private String label;

	private Long value;

}
