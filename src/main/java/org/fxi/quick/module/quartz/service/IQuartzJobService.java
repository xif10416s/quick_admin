package org.fxi.quick.module.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.fxi.quick.module.quartz.entity.QuartzJob;
import org.quartz.SchedulerException;

/**
 * @Description: 定时任务在线管理
 * @Author: jeecg-boot
 * @Date: 2019-04-28
 * @Version: V1.1
 */
public interface IQuartzJobService extends IService<QuartzJob> {

	/**
	 * 根据类名查找
	 * @param jobClassName
	 * @return
	 */
	List<QuartzJob> findByJobClassName(String jobClassName);

	/**
	 * 保存定时任务
	 * @param quartzJob
	 * @return
	 */
	boolean saveAndScheduleJob(QuartzJob quartzJob);

	/**
	 * 编辑定时任务
	 * @param quartzJob
	 * @return
	 * @throws SchedulerException
	 */
	boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException;

	/**
	 * 删除定时任务
	 * @param quartzJob
	 * @return
	 */
	boolean deleteAndStopJob(QuartzJob quartzJob);

	/**
	 * 恢复定时任务
	 * @param quartzJob
	 * @return
	 */
	boolean resumeJob(QuartzJob quartzJob);
}
