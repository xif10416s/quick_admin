package org.fxi.quick.module.oa.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.shiro.SecurityUtils;
import org.fxi.quick.common.api.vo.Result;
import org.fxi.quick.common.util.AccountContextUtil;
import org.fxi.quick.module.activiti.model.ProcessDefModel;
import org.fxi.quick.module.oa.convert.LeaveApplyConverter;
import org.fxi.quick.module.oa.entity.LeaveApply;
import org.fxi.quick.module.oa.model.LeaveApplyModel;
import org.fxi.quick.module.oa.service.ILeaveApplyService;
import org.fxi.quick.module.sys.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author initializer
 * @since 2020-06-21
 */
@Api(description = "oa办公api")
@Slf4j
@RestController
@RequestMapping("/oa/leaveApply")
public class LeaveApplyController {
  @Autowired
  private ILeaveApplyService leaveApplyService;

  @Autowired
  private RuntimeService runService;

  @Autowired
  private TaskService taskService;

  @ApiOperation(value = "请假申请", produces = "application/json")
  @PostMapping(value = "/startLeaveApply")
  public Result startLeaveApply(@RequestBody LeaveApplyModel leaveApplyModel) {
    leaveApplyModel.setCreateTime(LocalDateTime.now());
    leaveApplyModel.setUpdateTime(LocalDateTime.now());
    LeaveApply leaveApply = LeaveApplyConverter.INSTANCE.convertToEntity(leaveApplyModel);
    SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
    leaveApply.setUsername(sysUser.getUsername());
    leaveApplyService.startLeaveFlow(leaveApply);
    return Result.ok();
  }

  @ApiOperation(value = "获取请假详情", produces = "application/json")
  @GetMapping(value = "/getLeaveApplyModel")
  public Result<LeaveApplyModel> getLeaveApplyModel(@RequestParam(value = "processInstanceId") String processInstantId) {
    ProcessInstance processInstance = runService.createProcessInstanceQuery()
        .processInstanceId(processInstantId).singleResult();

    LeaveApply leaveApply = leaveApplyService.getBaseMapper()
        .selectById(Long.valueOf(processInstance.getBusinessKey()));
    Result<LeaveApplyModel> result = new Result<>();
    result.setResult(LeaveApplyConverter.INSTANCE.convertToModel(leaveApply));
    return result;
  }

  @ApiOperation(value = "审核请假", produces = "application/json")
  @GetMapping(value = "/audit")
  public Result doAudit(@RequestParam(value = "approval") Integer approval,@RequestParam(value = "taskId") String taskId) {
    Map<String, Object> variables = new HashMap<>(2);
    variables.put("leaderApprove", approval == 1 ? "true" : "false");
    variables.put("hrApprove", approval == 1 ? "true" : "false");
    String userName = AccountContextUtil.getAccountContext().getUserName();
    taskService.claim(taskId, userName);
    taskService.complete(taskId, variables);
    return Result.ok();
  }

  @ApiOperation(value = "销假", produces = "application/json")
  @GetMapping(value = "/reportBack")
  public Result reportBack(@RequestParam(value = "taskId") String taskId) {
    //更新业务对象
    taskService.complete(taskId);
    return Result.ok();
  }
}
