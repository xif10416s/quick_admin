package org.fxi.quick.module.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.fxi.quick.module.sys.entity.SysCategory;
import org.fxi.quick.module.sys.model.TreeSelectModel;

/**
 * @Description: 分类字典
 * @Author: jeecg-boot
 * @Date:   2019-05-29
 * @Version: V1.0
 */
public interface SysCategoryMapper extends BaseMapper<SysCategory> {

	/**
	  *  根据父级ID查询树节点数据
	 * @param pid
	 * @return
	 */
	public List<TreeSelectModel> queryListByPid(@Param("pid") Long pid,
      @Param("query") Map<String, String> query);

	/**
	 *
	 * @param code
	 * @return
	 */
	@Select("SELECT ID FROM sys_category WHERE CODE = #{code,jdbcType=VARCHAR}")
	public Long queryIdByCode(@Param("code") String code);


}
