package org.fxi.quick.module.sys.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.fxi.quick.common.api.vo.Result;
import org.fxi.quick.module.sys.convert.SysCategoryConverter;
import org.fxi.quick.module.sys.entity.SysCategory;
import org.fxi.quick.module.sys.model.DictModel;
import org.fxi.quick.module.sys.model.SysCategoryModel;
import org.fxi.quick.module.sys.model.SysCategorySearchModel;
import org.fxi.quick.module.sys.model.TreeSelectModel;
import org.fxi.quick.module.sys.service.ISysCategoryService;
import org.fxi.quick.module.sys.util.ParentIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* @Description: 分类字典
* @Author: jeecg-boot
* @Date:   2019-05-29
* @Version: V1.0
*/
@RestController
@RequestMapping("/sys/category")
@Slf4j
public class SysCategoryController {
 @Autowired
 private ISysCategoryService sysCategoryService;

 /**
   * 分页列表查询
  * @param sysCategorySearchModel
  * @return
  */
 @PostMapping(value = "/rootList")
 public Result<IPage<SysCategoryModel>> queryPageList(@RequestBody SysCategorySearchModel sysCategorySearchModel) {
   if(ParentIdUtils.isEmpty(sysCategorySearchModel.getPid())){
     sysCategorySearchModel.setPid(ParentIdUtils.EMPTY_ID);
   }
   Result<IPage<SysCategoryModel>> result = new Result<>();

   //--author:os_chengtgen---date:20190804 -----for: 分类字典页面显示错误,issues:377--------start
   //QueryWrapper<SysCategory> queryWrapper = QueryGenerator.initQueryWrapper(sysCategory, req.getParameterMap());
   QueryWrapper<SysCategory> queryWrapper = new QueryWrapper<>();
   queryWrapper.eq("pid", sysCategorySearchModel.getPid());
   //--author:os_chengtgen---date:20190804 -----for: 分类字典页面显示错误,issues:377--------end

   Page<SysCategory> page = new Page<SysCategory>(sysCategorySearchModel.getPageNo(), sysCategorySearchModel.getPageSize());
   IPage<SysCategoryModel> pageList = sysCategoryService.page(page, queryWrapper).convert(
       SysCategoryConverter.INSTANCE::convertToModel);
   result.setSuccess(true);
   result.setResult(pageList);
   return result;
 }

 @GetMapping(value = "/childList")
 public Result<List<SysCategory>> queryPageList(SysCategory sysCategory, HttpServletRequest req) {
   Result<List<SysCategory>> result = new Result<List<SysCategory>>();
   LambdaQueryWrapper<SysCategory> queryWrapper = new QueryWrapper<SysCategory>().lambda();
   if(sysCategory.getPid() != null){
     queryWrapper.eq(SysCategory::getPid,sysCategory.getPid());
   }
   List<SysCategory> list = sysCategoryService.list(queryWrapper);
   result.setSuccess(true);
   result.setResult(list);
   return result;
 }


 /**
   *   添加
  * @param sysCategory
  * @return
  */
 @PostMapping(value = "/add")
 public Result<SysCategory> add(@RequestBody SysCategory sysCategory) {
   Result<SysCategory> result = new Result<SysCategory>();
   try {
     sysCategoryService.addSysCategory(sysCategory);
     result.success("添加成功！");
   } catch (Exception e) {
     log.error(e.getMessage(),e);
     result.error500("操作失败");
   }
   return result;
 }

 /**
   *  编辑
  * @param sysCategory
  * @return
  */
 @PutMapping(value = "/edit")
 public Result<SysCategory> edit(@RequestBody SysCategory sysCategory) {
   Result<SysCategory> result = new Result<SysCategory>();
   SysCategory sysCategoryEntity = sysCategoryService.getById(sysCategory.getId());
   if(sysCategoryEntity==null) {
     result.error500("未找到对应实体");
   }else {
     sysCategoryService.updateSysCategory(sysCategory);
     result.success("修改成功!");
   }
   return result;
 }

 /**
   *   通过id删除
  * @param id
  * @return
  */
 @DeleteMapping(value = "/delete")
 public Result<SysCategory> delete(@RequestParam(name="id",required=true) String id) {
   Result<SysCategory> result = new Result<SysCategory>();
   SysCategory sysCategory = sysCategoryService.getById(id);
   if(sysCategory==null) {
     result.error500("未找到对应实体");
   }else {
     boolean ok = sysCategoryService.removeById(id);
     if(ok) {
       result.success("删除成功!");
     }
   }

   return result;
 }

 /**
   *  批量删除
  * @param ids
  * @return
  */
 @DeleteMapping(value = "/deleteBatch")
 public Result<SysCategory> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
   Result<SysCategory> result = new Result<SysCategory>();
   if(ids==null || "".equals(ids.trim())) {
     result.error500("参数不识别！");
   }else {
     this.sysCategoryService.removeByIds(Arrays.asList(ids.split(",")));
     result.success("删除成功!");
   }
   return result;
 }

 /**
   * 通过id查询
  * @param id
  * @return
  */
 @GetMapping(value = "/queryById")
 public Result<SysCategory> queryById(@RequestParam(name="id",required=true) String id) {
   Result<SysCategory> result = new Result<SysCategory>();
   SysCategory sysCategory = sysCategoryService.getById(id);
   if(sysCategory==null) {
     result.error500("未找到对应实体");
   }else {
     result.setResult(sysCategory);
     result.setSuccess(true);
   }
   return result;
 }




 /**
    * 加载单个数据 用于回显
  */
   @RequestMapping(value = "/loadOne", method = RequestMethod.GET)
  public Result<SysCategory> loadOne(@RequestParam(name="field") String field,@RequestParam(name="val") String val) {
    Result<SysCategory> result = new Result<SysCategory>();
    try {

      QueryWrapper<SysCategory> query = new QueryWrapper<SysCategory>();
      query.eq(field, val);
      List<SysCategory> ls = this.sysCategoryService.list(query);
      if(ls==null || ls.size()==0) {
        result.setMessage("查询无果");
         result.setSuccess(false);
      }else if(ls.size()>1) {
        result.setMessage("查询数据异常,["+field+"]存在多个值:"+val);
         result.setSuccess(false);
      }else {
        result.setSuccess(true);
        result.setResult(ls.get(0));
      }
    } catch (Exception e) {
      e.printStackTrace();
      result.setMessage(e.getMessage());
      result.setSuccess(false);
    }
    return result;
  }

   /**
         * 加载节点的子数据
    */
   @RequestMapping(value = "/loadTreeChildren", method = RequestMethod.GET)
 public Result<List<TreeSelectModel>> loadTreeChildren(@RequestParam(name="pid") Long pid) {
   Result<List<TreeSelectModel>> result = new Result<List<TreeSelectModel>>();
   try {
     List<TreeSelectModel> ls = this.sysCategoryService.queryListByPid(pid);
     result.setResult(ls);
     result.setSuccess(true);
   } catch (Exception e) {
     e.printStackTrace();
     result.setMessage(e.getMessage());
     result.setSuccess(false);
   }
   return result;
 }

   /**
        * 加载一级节点/如果是同步 则所有数据
    */
   @RequestMapping(value = "/loadTreeRoot", method = RequestMethod.GET)
    public Result<List<TreeSelectModel>> loadTreeRoot(@RequestParam(name="async") Boolean async,@RequestParam(name="pcode") String pcode) {
      Result<List<TreeSelectModel>> result = new Result<List<TreeSelectModel>>();
      try {
        List<TreeSelectModel> ls = this.sysCategoryService.queryListByCode(pcode);
        if(!async) {
          loadAllCategoryChildren(ls);
        }
        result.setResult(ls);
        result.setSuccess(true);
      } catch (Exception e) {
        e.printStackTrace();
        result.setMessage(e.getMessage());
        result.setSuccess(false);
      }
      return result;
    }

   /**
        * 递归求子节点 同步加载用到
    */
   private void loadAllCategoryChildren(List<TreeSelectModel> ls) {
     for (TreeSelectModel tsm : ls) {
     List<TreeSelectModel> temp = this.sysCategoryService.queryListByPid(tsm.getKey());
     if(temp!=null && temp.size()>0) {
       tsm.setChildren(temp);
       loadAllCategoryChildren(temp);
     }
   }
   }

  /**
   * 校验编码
   * @param pid
   * @param code
   * @return
   */
  @GetMapping(value = "/checkCode")
  public Result<?> checkCode(@RequestParam(name="pid",required = false) String pid,@RequestParam(name="code",required = false) String code) {
   if(StringUtils.isBlank(code)){
     return Result.error("错误,类型编码为空!");
   }
   if(StringUtils.isBlank(pid)){
     return Result.ok();
   }
   SysCategory parent = this.sysCategoryService.getById(pid);
   if(code.startsWith(parent.getCode())){
     return Result.ok();
   }else{
     return Result.error("编码不符合规范,须以\""+parent.getCode()+"\"开头!");
   }

  }


  /**
   * 分类字典树控件 加载节点
   * @param pid
   * @param pcode
   * @param condition
   * @return
   */
  @RequestMapping(value = "/loadTreeData", method = RequestMethod.GET)
  public Result<List<TreeSelectModel>> loadDict(@RequestParam(name="pid",required = false) Long pid,@RequestParam(name="pcode",required = false) String pcode, @RequestParam(name="condition",required = false) String condition) {
    Result<List<TreeSelectModel>> result = new Result<List<TreeSelectModel>>();
    //pid如果传值了 就忽略pcode的作用
    if(ParentIdUtils.isEmpty(pid)){
      if(StringUtils.isBlank(pcode)){
       result.setSuccess(false);
       result.setMessage("加载分类字典树参数有误.[null]!");
       return result;
     }else{
        if(ISysCategoryService.ROOT_PID_VALUE.equals(pcode)){
         pid = ParentIdUtils.EMPTY_ID;
       }else{
         pid = this.sysCategoryService.queryIdByCode(pcode);
       }
       if(ParentIdUtils.isEmpty(pid)){
         result.setSuccess(false);
         result.setMessage("加载分类字典树参数有误.[code]!");
         return result;
       }
     }
    }
    Map<String, String> query = null;
    if(StringUtils.isNotBlank(condition)) {
      query = JSONUtil.parseObj(condition).toBean(Map.class);
    }
    List<TreeSelectModel> ls = sysCategoryService.queryListByPid(pid,query);
    result.setSuccess(true);
    result.setResult(ls);
    return result;
  }

  /**
   * 分类字典控件数据回显[表单页面]
   * @return
   */
  @RequestMapping(value = "/loadDictItem", method = RequestMethod.GET)
  public Result<List<String>> loadDictItem(@RequestParam(name="ids") String ids) {
    Result<List<String>> result = new Result<>();
    LambdaQueryWrapper<SysCategory> query = new LambdaQueryWrapper<SysCategory>().in(SysCategory::getId,ids);
    List<SysCategory> list = this.sysCategoryService.list(query);
    List<String> textList = new ArrayList<String>();
    for (String id : ids.split(",")) {
      for (SysCategory c : list) {
       if(id.equals(c.getId())){
         textList.add(c.getName());
         break;
       }
      }
    }
    result.setSuccess(true);
    result.setResult(textList);
    return result;
  }


  /**
   * [列表页面]加载分类字典数据 用于值的替换
   * @param code
   * @return
   */
  @RequestMapping(value = "/loadAllData", method = RequestMethod.GET)
  public Result<List<DictModel>> loadAllData(@RequestParam(name="code",required = true) String code) {
    Result<List<DictModel>> result = new Result<List<DictModel>>();
    LambdaQueryWrapper<SysCategory> query = new LambdaQueryWrapper<SysCategory>();
    if(StringUtils.isNotBlank(code) && !"0".equals(code)){
      query.likeRight(SysCategory::getCode,code);
    }
    List<SysCategory> list = this.sysCategoryService.list(query);
    if(list==null || list.size()==0) {
      result.setMessage("无数据,参数有误.[code]");
      result.setSuccess(false);
      return result;
    }
    List<DictModel> rdList = new ArrayList<DictModel>();
    for (SysCategory c : list) {
      rdList.add(new DictModel(c.getId().toString(),c.getName()));
    }
    result.setSuccess(true);
    result.setResult(rdList);
    return result;
  }



}
