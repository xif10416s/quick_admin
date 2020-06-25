package org.fxi.quick.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;
import org.fxi.quick.common.exception.BizException;
import org.fxi.quick.module.sys.entity.SysCategory;
import org.fxi.quick.module.sys.model.TreeSelectModel;

/**
 * @Description: 分类字典
 * @Author: jeecg-boot
 * @Date:   2019-05-29
 * @Version: V1.0
 */
public interface ISysCategoryService extends IService<SysCategory> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";

	void addSysCategory(SysCategory sysCategory);

	void updateSysCategory(SysCategory sysCategory);

	/**
	  * 根据父级编码加载分类字典的数据
	 * @param pcode
	 * @return
	 */
	public List<TreeSelectModel> queryListByCode(String pcode) throws BizException;

	/**
	  * 根据pid查询子节点集合
	 * @param pid
	 * @return
	 */
	public List<TreeSelectModel> queryListByPid(Long pid);

	/**
	 * 根据pid查询子节点集合,支持查询条件
	 * @param pid
	 * @param condition
	 * @return
	 */
	public List<TreeSelectModel> queryListByPid(Long pid, Map<String, String> condition);

	/**
	 * 根据code查询id
	 * @param code
	 * @return
	 */
	public Long queryIdByCode(String code);

}
