package org.fxi.quick.sys.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import javax.annotation.Resource;
import org.fxi.quick.sys.entity.SysAnnouncementSend;
import org.fxi.quick.sys.mapper.SysAnnouncementSendMapper;
import org.fxi.quick.sys.model.AnnouncementSendModel;
import org.fxi.quick.sys.service.ISysAnnouncementSendService;
import org.springframework.stereotype.Service;

/**
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date:  2019-02-21
 * @Version: V1.0
 */
@Service
public class SysAnnouncementSendServiceImpl extends
    ServiceImpl<SysAnnouncementSendMapper, SysAnnouncementSend> implements
		ISysAnnouncementSendService {

	@Resource
	private SysAnnouncementSendMapper sysAnnouncementSendMapper;

	@Override
	public List<String> queryByUserId(Long userId) {
		return sysAnnouncementSendMapper.queryByUserId(userId);
	}

	@Override
	public Page<AnnouncementSendModel> getMyAnnouncementSendPage(Page<AnnouncementSendModel> page,
			AnnouncementSendModel announcementSendModel) {
		 return page.setRecords(sysAnnouncementSendMapper.getMyAnnouncementSendList(page, announcementSendModel));
	}

}
