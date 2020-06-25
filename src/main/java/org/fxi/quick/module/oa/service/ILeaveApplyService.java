package org.fxi.quick.module.oa.service;

import org.fxi.quick.module.oa.entity.LeaveApply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author initializer
 * @since 2020-06-21
 */
public interface ILeaveApplyService extends IService<LeaveApply> {

  /**
   * 开启请假流程
   * @param leaveApply
   */
  void startLeaveFlow(LeaveApply leaveApply);
}
