package org.fxi.quick.module.activiti.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.fxi.quick.common.api.vo.Result;
import org.fxi.quick.common.model.PageSearchModel;
import org.fxi.quick.module.activiti.model.ProcessDefModel;
import org.fxi.quick.module.activiti.model.ProcessDefSearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author fei
 */
@Api(description = "工作流API")
@Slf4j
@RestController
@RequestMapping("/activiti")
public class ActivitiController {

  @Autowired
  RepositoryService rep;
  @Autowired
  RuntimeService runService;
  @Autowired
  FormService formService;
  @Autowired
  IdentityService identityService;
  @Autowired
  TaskService taskService;
  @Autowired
  HistoryService historyService;


  @PostMapping(value = "/processList")
  @ApiOperation(value = "获取工作量定义列表", produces = "application/json")
  @ResponseBody
  public Result<IPage<ProcessDefModel>> processList(@RequestBody ProcessDefSearchModel processDefSearchModel) {
    int firstRow = (processDefSearchModel.getPageNo() - 1) * processDefSearchModel.getPageSize();
    List<ProcessDefinition> list = rep.createProcessDefinitionQuery()
        .listPage(firstRow, processDefSearchModel.getPageSize());
    int total = rep.createProcessDefinitionQuery().list().size();
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

  @GetMapping(value = "/deleteDeploy")
  @ResponseBody
  @ApiOperation(value = "按照部署id删除", produces = "application/json")
  public Result deleteDeploy(@RequestParam("deployId") String deployId) throws Exception {
    rep.deleteDeployment(deployId, true);
    return Result.ok();
  }


  @RequestMapping(value = "/processHis", method = RequestMethod.POST)
  @ResponseBody
  @ApiOperation(value = "获取历史工作流程的任务明细", produces = "application/json")
  public List<HistoricActivityInstance> processHis(@RequestParam("definitionKey") String definitionKey,@RequestParam("bizKey") String bizKey) {
    HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService
        .createHistoricProcessInstanceQuery();
    if( definitionKey  != null){
      historicProcessInstanceQuery.processDefinitionKey(definitionKey);
    }
    if(bizKey != null){
      historicProcessInstanceQuery.processInstanceBusinessKey(bizKey);
    }

    List<HistoricActivityInstance> rsList = new ArrayList<>(16);
    historicProcessInstanceQuery.list().stream().forEach(historicProcessInstance -> {
      List<HistoricActivityInstance> his = historyService
          .createHistoricActivityInstanceQuery().processInstanceId(historicProcessInstance.getId())
          .orderByHistoricActivityInstanceStartTime().asc().list();
      rsList.addAll(his);
    });
    return rsList;
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

  // TODO查看所有我发起的流程


  //
}
