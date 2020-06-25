package org.fxi.quick.module.activiti.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.fxi.quick.common.api.vo.Result;
import org.fxi.quick.common.model.PageSearchModel;
import org.fxi.quick.module.activiti.model.HistoryInstanceSearchModel;
import org.fxi.quick.module.activiti.model.ProcessDefModel;
import org.fxi.quick.module.activiti.model.ProcessDefSearchModel;
import org.fxi.quick.module.activiti.model.ProcessInstSearchModel;
import org.fxi.quick.module.activiti.model.ProcessInstanceModel;
import org.fxi.quick.module.activiti.model.TaskModel;
import org.fxi.quick.module.sys.entity.SysUser;
import org.fxi.quick.module.sys.model.SysRoleModel;
import org.fxi.quick.module.sys.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author fei
 */
@Api(description = "工作流API")
@Slf4j
@RestController
@RequestMapping("/activiti")
public class ActivitiController {

  @Autowired
  private RepositoryService rep;
  @Autowired
  private RuntimeService runService;
  @Autowired
  private FormService formService;
  @Autowired
  private IdentityService identityService;
  @Autowired
  private TaskService taskService;
  @Autowired
  private HistoryService historyService;
  @Autowired
  private ISysUserRoleService sysUserRoleService;

  @RequestMapping(value = "/uploadworkflow", method = RequestMethod.POST)
  public Result fileupload(@RequestParam MultipartFile uploadfile, HttpServletRequest request) {
    try {
      MultipartFile file = uploadfile;
      String filename = file.getOriginalFilename();
      InputStream is = file.getInputStream();
      rep.createDeployment().addInputStream(filename, is).deploy();
    } catch (Exception e) {
      e.printStackTrace();
      Result.error(201,"导入失败");
    }
    return Result.ok("导入成功");
  }

  @PostMapping(value = "/processList")
  @ApiOperation(value = "获取工作量定义列表", produces = "application/json")
  @ResponseBody
  public Result<IPage<ProcessDefModel>> processList(@RequestBody ProcessDefSearchModel processDefSearchModel) {
    int firstRow = (processDefSearchModel.getPageNo() - 1) * processDefSearchModel.getPageSize();
    ProcessDefinitionQuery processDefinitionQuery = rep.createProcessDefinitionQuery();
    if(StringUtils.isNotBlank(processDefSearchModel.getName())){
      processDefinitionQuery.processDefinitionNameLike("%"+processDefSearchModel.getName()+"%");
    }

    if(StringUtils.isNotBlank(processDefSearchModel.getCategory())){
      processDefinitionQuery.processDefinitionCategory(processDefSearchModel.getCategory());
    }

    if(processDefSearchModel.getSuspended() != null){
       if(processDefSearchModel.getSuspended() == 2){
         processDefinitionQuery.suspended();
       } else {
         processDefinitionQuery.active();
       }
    }

    List<ProcessDefinition> list = processDefinitionQuery
        .listPage(firstRow, processDefSearchModel.getPageSize());
    int total = processDefinitionQuery.list().size();
    Page<ProcessDefModel> page = new Page<>(processDefSearchModel.getPageNo(),
        processDefSearchModel.getPageSize());
    page.setTotal(total);
    page.setCurrent(processDefSearchModel.getPageNo());
    List<ProcessDefModel> processDefModelList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      ProcessDefModel p = new ProcessDefModel();
      ProcessDefinition pd = list.get(i);
      p.setDeploymentId(pd.getDeploymentId());
      p.setId(pd.getId());
      p.setKey(pd.getKey());
      p.setName(pd.getName());
      p.setResourceName(pd.getResourceName());
      p.setDiagramResourceName(pd.getDiagramResourceName());
      p.setCategory(pd.getCategory());
      p.setSuspended(pd.isSuspended() ? "否" : "是");
      processDefModelList.add(p);
    }
    page.setRecords(processDefModelList);
    Result<IPage<ProcessDefModel>> result = new Result<IPage<ProcessDefModel>>();
    result.setResult(page);
    return result;
  }


  @GetMapping(value = "/showResource")
  @ResponseBody
  @ApiOperation(value = "查看工作流定义", produces = "application/json")
  public void showResource(@RequestParam("processDefId") String processDefId, @RequestParam("resource") String resource,
      HttpServletResponse response) throws Exception {
    ProcessDefinition def = rep.createProcessDefinitionQuery().processDefinitionId(processDefId).singleResult();
    InputStream is = rep.getResourceAsStream(def.getDeploymentId(), resource);
    if(is != null) {
      ServletOutputStream output = response.getOutputStream();
      IOUtils.copy(is, output);
    }
  }

  @DeleteMapping(value = "/deleteDeploy")
  @ResponseBody
  @ApiOperation(value = "按照部署id删除", produces = "application/json")
  public Result deleteDeploy(@RequestParam("id") String deployId) throws Exception {
    rep.deleteDeployment(deployId, true);
    return Result.ok();
  }


  @PostMapping(value = "/processHis")
  @ResponseBody
  @ApiOperation(value = "获取历史工作流程的任务明细", produces = "application/json")
  public Result<IPage<HistoricProcessInstance>> processHis(@RequestBody HistoryInstanceSearchModel instanceSearchModel) {
    HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService
        .createHistoricProcessInstanceQuery();
    if( StringUtils.isNotBlank(instanceSearchModel.getCategory())){
      historicProcessInstanceQuery.processDefinitionCategory(instanceSearchModel.getCategory());
    }
    if(StringUtils.isNotBlank(instanceSearchModel.getDefinitionKey())){
      historicProcessInstanceQuery.processDefinitionKey(instanceSearchModel.getDefinitionKey());
    }

    int firstRow = (instanceSearchModel.getPageNo() - 1) * instanceSearchModel.getPageSize();
    Result<IPage<HistoricProcessInstance>> result = new Result<>();
    List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery
        .listPage(firstRow, instanceSearchModel.getPageSize());

    Page<HistoricProcessInstance> page = new Page<>(instanceSearchModel.getPageNo(),
        instanceSearchModel.getPageSize());
    page.setTotal(historicProcessInstanceQuery.list().size());
    page.setCurrent(instanceSearchModel.getPageNo());
    page.setRecords(historicProcessInstances);
    result.setResult(page);
    return result;
  }


//  /**
//   * 已经完成的请假申请流程
//   * @param session
//   * @param current
//   * @param rowCount
//   * @return
//   */
//  @RequestMapping(value = "/getfinishprocess", method = RequestMethod.POST)
//  @ResponseBody
//  public DataGrid<HistoryProcess> getHistory(HttpSession session, @RequestParam("current") int current,
//      @RequestParam("rowCount") int rowCount) {
//    String userid = (String) session.getAttribute("username");
//    HistoricProcessInstanceQuery process = histiryservice.createHistoricProcessInstanceQuery()
//        .processDefinitionKey("leave").startedBy(userid).finished();
//    int total = (int) process.count();
//    int firstrow = (current - 1) * rowCount;
//    List<HistoricProcessInstance> info = process.listPage(firstrow, rowCount);
//    List<HistoryProcess> list = new ArrayList<HistoryProcess>();
//    for (HistoricProcessInstance history : info) {
//      HistoryProcess his = new HistoryProcess();
//      String bussinesskey = history.getBusinessKey();
//      LeaveApply apply = leaveservice.getleave(Integer.parseInt(bussinesskey));
//      his.setLeaveapply(apply);
//      his.setBusinessKey(bussinesskey);
//      his.setProcessDefinitionId(history.getProcessDefinitionId());
//      list.add(his);
//    }
//    DataGrid<HistoryProcess> grid = new DataGrid<HistoryProcess>();
//    grid.setCurrent(current);
//    grid.setRowCount(rowCount);
//    grid.setTotal(total);
//    grid.setRows(list);
//    return grid;
//  }

  /**
   * 流程明细
   * @param instanceid
   * @return
   */
  @RequestMapping(value = "/processinfo", method = RequestMethod.POST)
  @ResponseBody
  public List<HistoricActivityInstance> processinfo(@RequestParam("instanceid") String instanceid) {
    List<HistoricActivityInstance> his = historyService.createHistoricActivityInstanceQuery()
        .processInstanceId(instanceid).orderByHistoricActivityInstanceStartTime().asc().list();
    return his;
  }


  @RequestMapping(value = "/processInstList", method = RequestMethod.POST)
  @ResponseBody
  @ApiOperation(value = "查看所有运行中流程实例", produces = "application/json")
  public Result<IPage<ProcessInstanceModel>> processInstList(@RequestBody ProcessInstSearchModel searchModel) {
    ProcessInstanceQuery processInstanceQuery = runService.createProcessInstanceQuery();
    if(StringUtils.isNotBlank(searchModel.getCategory())){
      processInstanceQuery.processDefinitionCategory(searchModel.getCategory());
    }

    if(StringUtils.isNotBlank(searchModel.getInvolvedUser())){
      processInstanceQuery.involvedUser(searchModel.getInvolvedUser());
    }

    if(searchModel.getSuspended() != null){
      if(searchModel.getSuspended() == 1){
        processInstanceQuery.active();
      } else {
        processInstanceQuery.suspended();
      }
    }
    int firstRow = (searchModel.getPageNo() - 1) * searchModel.getPageSize();

    List<ProcessInstance> list = processInstanceQuery
        .listPage(firstRow, searchModel.getPageSize());
    int total = processInstanceQuery.list().size();
    Page<ProcessInstanceModel> page = new Page<>(searchModel.getPageNo(),
        searchModel.getPageSize());

    Result<IPage<ProcessInstanceModel>> result = new Result<>();
    List<ProcessInstanceModel> modelList = new ArrayList<>(16);
    list.stream().forEach(inst ->{
      ProcessInstanceModel processInstanceModel = new ProcessInstanceModel();
      processInstanceModel.setBusinessKey(inst.getBusinessKey());
      processInstanceModel.setProcessDefinitionId(inst.getProcessDefinitionId());
      processInstanceModel.setStartUserId(inst.getStartUserId());
      Date date = inst.getStartTime();
      Instant instant = date.toInstant();
      ZoneId zoneId = ZoneId.systemDefault();
      LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
      processInstanceModel.setStartTime(localDateTime);

      processInstanceModel.setSuspended(inst.isSuspended()?"是":"否");
      processInstanceModel.setName(inst.getName());
      processInstanceModel.setProcessInstanceId(inst.getProcessInstanceId());
      modelList.add(processInstanceModel);
    });

    page.setTotal(total);
    page.setCurrent(searchModel.getPageNo());
    page.setRecords(modelList);
    result.setResult(page);
    return result;

  }
    // TODO查看所有我发起的流程

  @ApiOperation(value = "我的待处理任务", produces = "application/json")
  @PostMapping(value = "/todoTaskList")
  public Result<IPage<TaskModel>> todoTaskList(@RequestBody PageSearchModel pageSearchModel) {
    SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
    TaskQuery taskQuery = taskService.createTaskQuery();
    List<SysRoleModel> userRole = sysUserRoleService.getUserRole(sysUser.getId());
    // 直接指定assignee 或者 candidateUsers 的 用户名称
    taskQuery.taskCandidateOrAssigned(sysUser.getUsername());
    //  指定candidateGroups的角色名称
    if(userRole != null && userRole.size() >0){
      taskQuery.taskCandidateGroupIn(userRole.stream().map(item -> item.getRoleCode()).collect(
          Collectors.toList()));
    }

    int start = (pageSearchModel.getPageNo() - 1) * pageSearchModel.getPageSize();
    int limit = pageSearchModel.getPageSize();
    List<TaskModel> tasks = taskQuery
        .orderByTaskPriority().desc().orderByTaskCreateTime().desc()
        .listPage(start, limit).stream().map(item ->{
          TaskModel taskModel = new TaskModel();
          taskModel.setId(item.getId());
          taskModel.setAssignee(item.getAssignee());
          taskModel.setCategory(item.getCategory());
          Date createTime = item.getCreateTime();
          Instant instant = createTime.toInstant();
          ZoneId zoneId = ZoneId.systemDefault();
          taskModel.setCreateTime(LocalDateTime.ofInstant(instant,zoneId));
          taskModel.setDescription(item.getDescription());
          if(item.getDueDate() != null){
            taskModel.setDueDate(LocalDateTime.ofInstant(item.getDueDate().toInstant(),zoneId));
          }

          taskModel.setName(item.getName());
          taskModel.setPriority(item.getPriority());
          taskModel.setSuspended(item.isSuspended() ? "否" : "是");
          taskModel.setTaskDefinitionKey(item.getTaskDefinitionKey());
          taskModel.setProcessInstanceId(item.getProcessInstanceId());
          taskModel.setOwner(item.getOwner());
          return taskModel;
        }).collect(Collectors.toList());

    Page<TaskModel> page = new Page<>(pageSearchModel.getPageNo(),
        pageSearchModel.getPageSize());
    page.setTotal(taskQuery.list().size());
    page.setCurrent(pageSearchModel.getPageNo());
    page.setRecords(tasks);
    Result<IPage<TaskModel>> result = new Result<>();
    result.setResult(page);
    return result;
  }

  @ApiOperation(value = "查看任务执行进度", produces = "application/json")
  @RequestMapping(value = "traceProcess", method = RequestMethod.GET)
  public void traceProcess(@RequestParam("processDefinitionId") String processDefinitionId , @RequestParam("processInstanceId") String processInstanceId , HttpServletResponse response)
      throws Exception {
    // 设置页面不缓存
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);

    ProcessDefinitionQuery pdq = rep.createProcessDefinitionQuery();
    ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();
    String resourceName = pd.getDiagramResourceName();
    if(resourceName.endsWith(".png") && StringUtils.isBlank(processInstanceId) == false)
    {
      getActivitiProccessImage(processInstanceId,response);
      //ProcessDiagramGenerator.generateDiagram(pde, "png", getRuntimeService().getActiveActivityIds(processInstanceId));
    }
    else
    {
      // 通过接口读取
      InputStream resourceAsStream = rep.getResourceAsStream(pd.getDeploymentId(), resourceName);

      // 输出资源内容到相应对象
      byte[] b = new byte[1024];
      int len = -1;
      while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
        response.getOutputStream().write(b, 0, len);
      }
    }
  }

  /**
   * 获取流程图像，已执行节点和流程线高亮显示
   */
  public void getActivitiProccessImage(String pProcessInstanceId, HttpServletResponse response)
  {
    //logger.info("[开始]-获取流程图图像");
    try {
      //  获取历史流程实例
      HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
          .processInstanceId(pProcessInstanceId).singleResult();

      if (historicProcessInstance == null) {
        //throw new BusinessException("获取流程实例ID[" + pProcessInstanceId + "]对应的历史流程实例失败！");
      }
      else
      {
        // 获取流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) rep)
            .getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());

        // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(pProcessInstanceId).orderByHistoricActivityInstanceId().asc().list();

        // 已执行的节点ID集合
        List<String> executedActivityIdList = new ArrayList<String>();
        int index = 1;
        //logger.info("获取已经执行的节点ID");
        for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
          executedActivityIdList.add(activityInstance.getActivityId());

          //logger.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : " +activityInstance.getActivityName());
          index++;
        }

        BpmnModel bpmnModel = rep.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

        // 已执行的线集合
        List<String> flowIds = new ArrayList<String>();
        // 获取流程走过的线 (getHighLightedFlows是下面的方法)
        flowIds = getHighLightedFlows(bpmnModel,processDefinition, historicActivityInstanceList);



        // 获取流程图图像字符流
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessDiagramGenerator pec = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
        //配置字体
        InputStream imageStream = pec.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds,"宋体","微软雅黑","黑体",null,2.0);

        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
          os.write(buffer, 0, bytesRead);
        }
        os.close();
        imageStream.close();
      }
      //logger.info("[完成]-获取流程图图像");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      //logger.error("【异常】-获取流程图失败！" + e.getMessage());
      //throw new BusinessException("获取流程图失败！" + e.getMessage());
    }
  }

  public List<String> getHighLightedFlows(BpmnModel bpmnModel,ProcessDefinitionEntity processDefinitionEntity,List<HistoricActivityInstance> historicActivityInstances)
  {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //24小时制
    List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId

    for (int i = 0; i < historicActivityInstances.size() - 1; i++)
    {
      // 对历史流程节点进行遍历
      // 得到节点定义的详细信息
      FlowNode activityImpl = (FlowNode)bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i).getActivityId());


      List<FlowNode> sameStartTimeNodes = new ArrayList<FlowNode>();// 用以保存后续开始时间相同的节点
      FlowNode sameActivityImpl1 = null;

      HistoricActivityInstance activityImpl_ = historicActivityInstances.get(i);// 第一个节点
      HistoricActivityInstance activityImp2_ ;

      for(int k = i + 1 ; k <= historicActivityInstances.size() - 1; k++)
      {
        activityImp2_ = historicActivityInstances.get(k);// 后续第1个节点

        if ( activityImpl_.getActivityType().equals("userTask") && activityImp2_.getActivityType().equals("userTask") &&
            df.format(activityImpl_.getStartTime()).equals(df.format(activityImp2_.getStartTime()))   ) //都是usertask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
        {

        }
        else
        {
          sameActivityImpl1 = (FlowNode)bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(k).getActivityId());//找到紧跟在后面的一个节点
          break;
        }

      }
      sameStartTimeNodes.add(sameActivityImpl1); // 将后面第一个节点放在时间相同节点的集合里
      for (int j = i + 1; j < historicActivityInstances.size() - 1; j++)
      {
        HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
        HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点

        if (df.format(activityImpl1.getStartTime()).equals(df.format(activityImpl2.getStartTime()))  )
        {// 如果第一个节点和第二个节点开始时间相同保存
          FlowNode sameActivityImpl2 = (FlowNode)bpmnModel.getMainProcess().getFlowElement(activityImpl2.getActivityId());
          sameStartTimeNodes.add(sameActivityImpl2);
        }
        else
        {// 有不相同跳出循环
          break;
        }
      }
      List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows() ; // 取出节点的所有出去的线

      for (SequenceFlow pvmTransition : pvmTransitions)
      {// 对所有的线进行遍历
        FlowNode pvmActivityImpl = (FlowNode)bpmnModel.getMainProcess().getFlowElement( pvmTransition.getTargetRef());// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
        if (sameStartTimeNodes.contains(pvmActivityImpl)) {
          highFlows.add(pvmTransition.getId());
        }
      }

    }
    return highFlows;

  }

  //流程定义的挂起与激活
  /**
   * repositoryService.suspendProcessDefinitionByKey("vacationRequest");
   * repositoryService.activateProcessDefinitionByKey("vacationRequest");
   */
}
