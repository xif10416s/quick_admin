package org.fxi.quick.module.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.fxi.quick.module.sys.entity.SysUserDepart;

public interface SysUserDepartMapper extends BaseMapper<SysUserDepart> {

	List<SysUserDepart> getUserDepartByUid(@Param("userId") Long userId);
}
