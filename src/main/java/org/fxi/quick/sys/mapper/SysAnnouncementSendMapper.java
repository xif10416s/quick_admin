package org.fxi.quick.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.fxi.quick.sys.entity.SysAnnouncementSend;
import org.fxi.quick.sys.model.AnnouncementSendModel;

/**
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date:  2019-02-21
 * @Version: V1.0
 */
public interface SysAnnouncementSendMapper extends BaseMapper<SysAnnouncementSend> {

	public List<String> queryByUserId(@Param("userId") Long userId);

	/**
	 * @功能：获取我的消息
	 * @param announcementSendModel
	 * @return
	 */
	public List<AnnouncementSendModel> getMyAnnouncementSendList(Page<AnnouncementSendModel> page,
      @Param("announcementSendModel") AnnouncementSendModel announcementSendModel);

}
