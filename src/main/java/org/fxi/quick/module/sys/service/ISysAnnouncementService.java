package org.fxi.quick.module.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.fxi.quick.module.sys.entity.SysAnnouncement;

/**
 * @Description: 系统通告表
 * @Author: jeecg-boot
 * @Date:  2019-01-02
 * @Version: V1.0
 */
public interface ISysAnnouncementService extends IService<SysAnnouncement> {

	public void saveAnnouncement(SysAnnouncement sysAnnouncement);

	public boolean updateAnnouncement(SysAnnouncement sysAnnouncement);

	public Page<SysAnnouncement> querySysCementPageByUserId(Page<SysAnnouncement> page, Long userId,
      String msgCategory);

	public void saveSysAnnouncement(String title, String msgContent);
}
