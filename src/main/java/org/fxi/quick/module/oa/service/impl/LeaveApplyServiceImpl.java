package org.fxi.quick.module.oa.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.shiro.SecurityUtils;
import org.fxi.quick.module.oa.convert.LeaveApplyConverter;
import org.fxi.quick.module.oa.entity.LeaveApply;
import org.fxi.quick.module.oa.mapper.LeaveApplyMapper;
import org.fxi.quick.module.oa.service.ILeaveApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fxi.quick.module.sys.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author initializer
 * @since 2020-06-21
 */
@Service
public class LeaveApplyServiceImpl extends ServiceImpl<LeaveApplyMapper, LeaveApply> implements ILeaveApplyService {

  @Autowired
  private IdentityService identityservice;
  @Autowired
  private RuntimeService runtimeservice;
  @Autowired
  private TaskService taskservice;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void startLeaveFlow(LeaveApply leaveApply) {
    saveOrUpdate(leaveApply);
    String bizKey = leaveApply.getId().toString();
    identityservice.setAuthenticatedUserId(leaveApply.getUsername());
    Map<String, Object> variables = new HashMap<>(16);
    ProcessInstance instance=runtimeservice.startProcessInstanceByKey("leaveApply",bizKey,variables);
    leaveApply.setProcessInstanceId(instance.getId());
    saveOrUpdate(leaveApply);
  }
}
