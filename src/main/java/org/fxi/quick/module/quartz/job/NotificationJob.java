package org.fxi.quick.module.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.fxi.quick.module.sys.service.ISysUserService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * @Author fei
 */
@Component
@Slf4j
public class NotificationJob extends QuartzJobBean {

  public static final  String JOB_DEFINE_KEY = "job_define_key";

  @Autowired
  private ISysUserService sysUserService;

  @Override
  protected void executeInternal(JobExecutionContext jobExecutionContext)
      throws JobExecutionException {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();


  }
}
