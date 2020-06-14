package org.fxi.quick.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import org.fxi.quick.common.constant.CommonConstant;
import org.fxi.quick.sys.entity.SysAnnouncement;
import org.fxi.quick.sys.entity.SysAnnouncementSend;
import org.fxi.quick.sys.mapper.SysAnnouncementMapper;
import org.fxi.quick.sys.mapper.SysAnnouncementSendMapper;
import org.fxi.quick.sys.service.ISysAnnouncementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 系统通告表
 * @Author: jeecg-boot
 * @Date:  2019-01-02
 * @Version: V1.0
 */
@Service
public class SysAnnouncementServiceImpl extends
    ServiceImpl<SysAnnouncementMapper, SysAnnouncement> implements ISysAnnouncementService {

	@Resource
	private SysAnnouncementSendMapper sysAnnouncementSendMapper;

	@Transactional
	@Override
	public void saveAnnouncement(SysAnnouncement sysAnnouncement) {
		if(sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {
			this.getBaseMapper().insert(sysAnnouncement);
		}else {
			// 1.插入通告表记录
			this.getBaseMapper().insert(sysAnnouncement);
			// 2.插入用户通告阅读标记表记录
			String userId = sysAnnouncement.getUserIds();
			String[] userIds = userId.substring(0, (userId.length()-1)).split(",");
			Long anntId = sysAnnouncement.getId();
			LocalDateTime refDate = LocalDateTime.now();
			for(int i=0;i<userIds.length;i++) {
				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
				announcementSend.setAnntId(anntId);
				announcementSend.setUserId(Long.valueOf(userIds[i]));
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				announcementSend.setReadTime(refDate);
				sysAnnouncementSendMapper.insert(announcementSend);
			}
		}
	}

	/**
	 * @功能：编辑消息信息
	 */
	@Transactional
	@Override
	public boolean updateAnnouncement(SysAnnouncement sysAnnouncement) {
		// 1.更新系统信息表数据
		this.getBaseMapper().updateById(sysAnnouncement);
		String userId = sysAnnouncement.getUserIds();
		if(StringUtils.isNotBlank(userId)&&sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_UESR)) {
			// 2.补充新的通知用户数据
			String[] userIds = userId.substring(0, (userId.length()-1)).split(",");
			Long anntId = sysAnnouncement.getId();
			LocalDateTime refDate = LocalDateTime.now();
			for(int i=0;i<userIds.length;i++) {
				LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<SysAnnouncementSend>();
				queryWrapper.eq(SysAnnouncementSend::getAnntId, anntId);
				queryWrapper.eq(SysAnnouncementSend::getUserId, userIds[i]);
				List<SysAnnouncementSend> announcementSends=sysAnnouncementSendMapper.selectList(queryWrapper);
				if(announcementSends.size()<=0) {
					SysAnnouncementSend announcementSend = new SysAnnouncementSend();
					announcementSend.setAnntId(anntId);
					announcementSend.setUserId(Long.valueOf(userIds[i]));
					announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
					announcementSend.setReadTime(refDate);
					sysAnnouncementSendMapper.insert(announcementSend);
				}
			}
			// 3. 删除多余通知用户数据
			Collection<String> delUserIds = Arrays.asList(userIds);
			LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<SysAnnouncementSend>();
			queryWrapper.notIn(SysAnnouncementSend::getUserId, delUserIds);
			queryWrapper.eq(SysAnnouncementSend::getAnntId, anntId);
			sysAnnouncementSendMapper.delete(queryWrapper);
		}
		return true;
	}

	// @功能：流程执行完成保存消息通知
	@Override
	public void saveSysAnnouncement(String title, String msgContent) {
		SysAnnouncement announcement = new SysAnnouncement();
		announcement.setTitle(title);
		announcement.setMsgContent(msgContent);
		announcement.setSender("JEECG BOOT");
		announcement.setPriority(CommonConstant.PRIORITY_L);
		announcement.setMsgType(CommonConstant.MSG_TYPE_ALL);
		announcement.setSendStatus(CommonConstant.HAS_SEND);
		announcement.setSendTime(LocalDateTime.now());
		announcement.setDelFlag(CommonConstant.DEL_FLAG_0);
		this.getBaseMapper().insert(announcement);
	}

	@Override
	public Page<SysAnnouncement> querySysCementPageByUserId(Page<SysAnnouncement> page, Long userId,String msgCategory) {
		 return page.setRecords(this.getBaseMapper().querySysCementListByUserId(page, userId, msgCategory));
	}

}
