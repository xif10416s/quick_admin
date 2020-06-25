package org.fxi.quick.module.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.fxi.quick.common.api.vo.Result;
import org.fxi.quick.module.sys.convert.SysPositionConverter;
import org.fxi.quick.module.sys.entity.SysPosition;
import org.fxi.quick.module.sys.model.SysPositionModel;
import org.fxi.quick.module.sys.model.SysPositionSearchModel;
import org.fxi.quick.module.sys.service.ISysPositionService;
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
 * @Description: 职务表
 * @Author: jeecg-boot
 * @Date: 2019-09-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "职务表")
@RestController
@RequestMapping("/sys/position")
public class SysPositionController {

    @Autowired
    private ISysPositionService sysPositionService;

    /**
     * 分页列表查询
     *
     * @param sysPositionSearchModel
     * @return
     */
    @ApiOperation(value = "职务表-分页列表查询", notes = "职务表-分页列表查询")
    @PostMapping(value = "/list")
    public Result<IPage<SysPositionModel>> queryPageList(@RequestBody SysPositionSearchModel sysPositionSearchModel) {
        Result<IPage<SysPositionModel>> result = new Result<>();
        LambdaQueryWrapper<SysPosition> queryWrapper = new QueryWrapper<SysPosition>().lambda();
        if(StringUtils.isNotBlank(sysPositionSearchModel.getName())){
            queryWrapper.eq(SysPosition::getName,sysPositionSearchModel.getName());
        }

        if(StringUtils.isNotBlank(sysPositionSearchModel.getCode())){
            queryWrapper.eq(SysPosition::getCode,sysPositionSearchModel.getCode());
        }

        if(StringUtils.isNotBlank(sysPositionSearchModel.getPostRank())){
            queryWrapper.eq(SysPosition::getPostRank,sysPositionSearchModel.getPostRank());
        }
        Page<SysPosition> page = new Page<>(sysPositionSearchModel.getPageNo(), sysPositionSearchModel.getPageSize());
        IPage<SysPositionModel> pageList = sysPositionService.page(page, queryWrapper).convert(
            SysPositionConverter.INSTANCE::convertToModel);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param sysPosition
     * @return
     */
    @ApiOperation(value = "职务表-添加", notes = "职务表-添加")
    @PostMapping(value = "/add")
    public Result<SysPosition> add(@RequestBody SysPosition sysPosition) {
        Result<SysPosition> result = new Result<>();
        try {
            sysPositionService.save(sysPosition);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     *
     * @param sysPosition
     * @return
     */
    @ApiOperation(value = "职务表-编辑", notes = "职务表-编辑")
    @PutMapping(value = "/edit")
    public Result<SysPosition> edit(@RequestBody SysPosition sysPosition) {
        Result<SysPosition> result = new Result<SysPosition>();
        SysPosition sysPositionEntity = sysPositionService.getById(sysPosition.getId());
        if (sysPositionEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysPositionService.updateById(sysPosition);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "职务表-通过id删除", notes = "职务表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            sysPositionService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "职务表-批量删除", notes = "职务表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<SysPosition> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<SysPosition> result = new Result<SysPosition>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysPositionService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "职务表-通过id查询", notes = "职务表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<SysPosition> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysPosition> result = new Result<SysPosition>();
        SysPosition sysPosition = sysPositionService.getById(id);
        if (sysPosition == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysPosition);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
//    @RequestMapping(value = "/exportXls")
//    public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
//        // Step.1 组装查询条件
//        QueryWrapper<SysPosition> queryWrapper = null;
//        try {
//            String paramsStr = request.getParameter("paramsStr");
//            if (oConvertUtils.isNotEmpty(paramsStr)) {
//                String deString = URLDecoder.decode(paramsStr, "UTF-8");
//                SysPosition sysPosition = JSON.parseObject(deString, SysPosition.class);
//                queryWrapper = QueryGenerator.initQueryWrapper(sysPosition, request.getParameterMap());
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        //Step.2 AutoPoi 导出Excel
//        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
//        List<SysPosition> pageList = sysPositionService.list(queryWrapper);
//        //导出文件名称
//        mv.addObject(NormalExcelConstants.FILE_NAME, "职务表列表");
//        mv.addObject(NormalExcelConstants.CLASS, SysPosition.class);
//        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("职务表列表数据", "导出人:Jeecg", "导出信息"));
//        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
//        return mv;
//    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
//    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response)throws IOException {
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
//        // 错误信息
//        List<String> errorMessage = new ArrayList<>();
//        int successLines = 0, errorLines = 0;
//        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
//            MultipartFile file = entity.getValue();// 获取上传文件对象
//            ImportParams params = new ImportParams();
//            params.setTitleRows(2);
//            params.setHeadRows(1);
//            params.setNeedSave(true);
//            try {
//                List<Object>  listSysPositions = ExcelImportUtil.importExcel(file.getInputStream(), SysPosition.class, params);
//                List<String> list = ImportExcelUtil.importDateSave(listSysPositions, ISysPositionService.class, errorMessage,CommonConstant.SQL_INDEX_UNIQ_CODE);
//                errorLines+=list.size();
//                successLines+=(listSysPositions.size()-errorLines);
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//                return Result.error("文件导入失败:" + e.getMessage());
//            } finally {
//                try {
//                    file.getInputStream().close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return ImportExcelUtil.imporReturnRes(errorLines,successLines,errorMessage);
//    }

}
