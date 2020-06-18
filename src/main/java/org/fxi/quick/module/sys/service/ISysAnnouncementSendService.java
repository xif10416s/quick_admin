package org.fxi.quick.module.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.fxi.quick.module.sys.entity.SysAnnouncementSend;
import org.fxi.quick.module.sys.model.AnnouncementSendModel;

/**
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date:  2019-02-21
 * @Version: V1.0
 */
public interface ISysAnnouncementSendService extends IService<SysAnnouncementSend> {

	public List<String> queryByUserId(Long userId);

	/**
	 * @功能：获取我的消息
	 * @param announcementSendModel
	 * @return
	 */
	public Page<AnnouncementSendModel> getMyAnnouncementSendPage(Page<AnnouncementSendModel> page,
      AnnouncementSendModel announcementSendModel);

}
