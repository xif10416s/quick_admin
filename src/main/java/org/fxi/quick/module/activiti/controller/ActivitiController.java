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
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.fxi.quick.common.api.vo.Result;
import org.fxi.quick.common.model.PageSearchModel;
import org.fxi.quick.module.activiti.model.ProcessDefModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
  public Result<IPage<ProcessDefModel>> processList(@RequestBody PageSearchModel pageSearchModel) {
    int firstRow = (pageSearchModel.getPageNo() - 1) * pageSearchModel.getPageSize();
    List<ProcessDefinition> list = rep.createProcessDefinitionQuery()
        .listPage(firstRow, pageSearchModel.getPageSize());
    int total = rep.createProcessDefinitionQuery().list().size();
    Page<ProcessDefModel> page = new Page<>(pageSearchModel.getPageNo(),
        pageSearchModel.getPageSize());
    page.setTotal(total);
    page.setCurrent(pageSearchModel.getPageNo());
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
      p.setSuspended(pd.isSuspended());
      processDefModelList.add(p);
    }
    page.setRecords(processDefModelList);
    Result<IPage<ProcessDefModel>> result = new Result<IPage<ProcessDefModel>>();
    result.setResult(page);
    return result;
  }


  @GetMapping(value = "/showResource")
  @ResponseBody
  @ApiOperation(value = "退出登录", produces = "application/json")
  public void showResource(@RequestParam("processDefId") String processDefId, @RequestParam("resource") String resource,
      HttpServletResponse response) throws Exception {
    ProcessDefinition def = rep.createProcessDefinitionQuery().processDefinitionId(processDefId).singleResult();
    InputStream is = rep.getResourceAsStream(def.getDeploymentId(), resource);
    if(is != null) {
      ServletOutputStream output = response.getOutputStream();
      IOUtils.copy(is, output);
    }
  }

}
