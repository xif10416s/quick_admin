package org.fxi.quick.module.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.fxi.quick.module.sys.entity.SysAnnouncement;

/**
 * @Description: 系统通告表
 * @Author: jeecg-boot
 * @Date:  2019-01-02
 * @Version: V1.0
 */
public interface SysAnnouncementMapper extends BaseMapper<SysAnnouncement> {


	List<SysAnnouncement> querySysCementListByUserId(Page<SysAnnouncement> page,
      @Param("userId") Long userId, @Param("msgCategory") String msgCategory);

}
