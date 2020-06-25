package org.fxi.quick.module.sys.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import org.fxi.quick.common.exception.BizException;
import org.fxi.quick.module.sys.entity.SysCategory;
import org.fxi.quick.module.sys.mapper.SysCategoryMapper;
import org.fxi.quick.module.sys.model.TreeSelectModel;
import org.fxi.quick.module.sys.rule.CategoryCodeRule;
import org.fxi.quick.module.sys.service.ISysCategoryService;
import org.fxi.quick.module.sys.util.ParentIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 分类字典
 * @Author: jeecg-boot
 * @Date:   2019-05-29
 * @Version: V1.0
 */
@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory> implements
		ISysCategoryService {

	@Autowired
	private CategoryCodeRule categoryCodeRule;

	private static final Short HAS_CHILD = Short.valueOf("1");

	@Override
	public void addSysCategory(SysCategory sysCategory) {
		String categoryCode = "";
		Long categoryPid = ParentIdUtils.EMPTY_ID;
		String parentCode = null;
		if(ParentIdUtils.isNotEmpty(sysCategory.getPid())){
			categoryPid = sysCategory.getPid();

			//PID 不是根节点 说明需要设置父节点 hasChild 为1
			if(!ISysCategoryService.ROOT_PID_VALUE.equals(categoryPid)){
				SysCategory parent = baseMapper.selectById(categoryPid);
				parentCode = parent.getCode();
				if(parent!=null && !HAS_CHILD.equals(parent.getHasChild())){
					parent.setHasChild(HAS_CHILD);
					baseMapper.updateById(parent);
				}
			}
		}
		//update-begin--Author:baihailong  Date:20191209 for：分类字典编码规则生成器做成公用配置
		JSONObject formData = new JSONObject();
		formData.put("pid",categoryPid);
		categoryCode = (String) categoryCodeRule.execute(formData);
		//update-end--Author:baihailong  Date:20191209 for：分类字典编码规则生成器做成公用配置
		sysCategory.setCode(categoryCode);
		sysCategory.setPid(categoryPid);
		baseMapper.insert(sysCategory);
	}

	@Override
	public void updateSysCategory(SysCategory sysCategory) {
		if(ParentIdUtils.isEmpty(sysCategory.getPid())){
			sysCategory.setPid(ParentIdUtils.EMPTY_ID);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChild 为1
			SysCategory parent = baseMapper.selectById(sysCategory.getPid());
			if(parent!=null && ! HAS_CHILD.equals(parent.getHasChild())){
				parent.setHasChild(HAS_CHILD);
				baseMapper.updateById(parent);
			}
		}
		baseMapper.updateById(sysCategory);
	}

	@Override
	public List<TreeSelectModel> queryListByCode(String pcode) throws BizException {
		Long pid = ParentIdUtils.EMPTY_ID;
		if(StringUtils.isNotBlank(pcode)) {
			List<SysCategory> list = baseMapper.selectList(new LambdaQueryWrapper<SysCategory>().eq(SysCategory::getCode, pcode));
			if(list==null || list.size() ==0) {
				throw new BizException("该编码【"+pcode+"】不存在，请核实!");
			}
			if(list.size()>1) {
				throw new BizException("该编码【"+pcode+"】存在多个，请核实!");
			}
			pid = list.get(0).getId();
		}
		return baseMapper.queryListByPid(pid,null);
	}

	@Override
	public List<TreeSelectModel> queryListByPid(Long pid) {
		if(ParentIdUtils.isEmpty(pid)) {
			pid = ParentIdUtils.EMPTY_ID;
		}
		return baseMapper.queryListByPid(pid,null);
	}

	@Override
	public List<TreeSelectModel> queryListByPid(Long pid, Map<String, String> condition) {
		if(ParentIdUtils.isEmpty(pid)) {
			pid = ParentIdUtils.EMPTY_ID;
		}
		return baseMapper.queryListByPid(pid,condition);
	}

	@Override
	public Long queryIdByCode(String code) {
		return baseMapper.queryIdByCode(code);
	}

}
