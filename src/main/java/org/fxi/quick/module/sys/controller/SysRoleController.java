package org.fxi.quick.module.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.fxi.quick.common.api.vo.Result;
import org.fxi.quick.common.constant.CommonConstant;
import org.fxi.quick.module.sys.convert.SysRoleConverter;
import org.fxi.quick.module.sys.entity.SysPermission;
import org.fxi.quick.module.sys.entity.SysRole;
import org.fxi.quick.module.sys.model.SysRoleModel;
import org.fxi.quick.module.sys.model.SysRoleSearchModel;
import org.fxi.quick.module.sys.model.TreeModel;
import org.fxi.quick.module.sys.service.ISysPermissionService;
import org.fxi.quick.module.sys.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
@RestController
@RequestMapping("/sys/role")
@Slf4j
public class SysRoleController {
	@Autowired
	private ISysRoleService sysRoleService;




	@Autowired
	private ISysPermissionService sysPermissionService;

	/**
	  * 分页列表查询
	 * @param sysRoleSearchModel
	 * @return
	 */
	@PostMapping(value = "/list")
	public Result<IPage<SysRoleModel>> queryPageList(@RequestBody SysRoleSearchModel sysRoleSearchModel) {
		Result<IPage<SysRoleModel>> result = new Result<>();
		QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
		if(StringUtils.isNotBlank(sysRoleSearchModel.getRoleName())){
			queryWrapper.lambda().like(SysRole::getRoleName,sysRoleSearchModel.getRoleName());
		}
		Page<SysRole> page = new Page<SysRole>(sysRoleSearchModel.getPageNo(), sysRoleSearchModel.getPageSize());
		IPage<SysRoleModel> pageList = sysRoleService.page(page, queryWrapper).convert(SysRoleConverter.INSTANCE::convertToModel);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	  *   添加
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	//@RequiresRoles({"admin"})
	public Result<SysRole> add(@RequestBody SysRole role) {
		Result<SysRole> result = new Result<SysRole>();
		try {
			role.setCreateTime(LocalDateTime.now());
			role.setDelFlag(Short.valueOf("0"));
			sysRoleService.save(role);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	  *  编辑
	 * @param role
	 * @return
	 */
	//@RequiresRoles({"admin"})
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public Result<SysRole> edit(@RequestBody SysRole role) {
		Result<SysRole> result = new Result<SysRole>();
		SysRole sysrole = sysRoleService.getById(role.getId());
		if(sysrole==null) {
			result.error500("未找到对应实体");
		}else {
			role.setUpdateTime(LocalDateTime.now());
			boolean ok = sysRoleService.updateById(role);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}

		return result;
	}

	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	//@RequiresRoles({"admin"})
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysRoleService.deleteRole(id);
		return Result.ok("删除角色成功");
	}

	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	//@RequiresRoles({"admin"})
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	public Result<SysRole> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysRole> result = new Result<SysRole>();
		if(StringUtils.isBlank(ids)) {
			result.error500("未选中角色！");
		}else {
			sysRoleService.deleteBatchRole(ids.split(","));
			result.success("删除角色成功!");
		}
		return result;
	}

	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/queryById", method = RequestMethod.GET)
	public Result<SysRole> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysRole> result = new Result<SysRole>();
		SysRole sysrole = sysRoleService.getById(id);
		if(sysrole==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysrole);
			result.setSuccess(true);
		}
		return result;
	}

	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
	public Result<List<SysRole>> queryall() {
		Result<List<SysRole>> result = new Result<>();
		List<SysRole> list = sysRoleService.list();
		if(list==null||list.size()<=0) {
			result.error500("未找到角色信息");
		}else {
			result.setResult(list);
			result.setSuccess(true);
		}
		return result;
	}

	/**
	  * 校验角色编码唯一
	 */
	@RequestMapping(value = "/checkRoleCode", method = RequestMethod.GET)
	public Result<Boolean> checkUsername(String id,String roleCode) {
		Result<Boolean> result = new Result<>();
		result.setResult(true);//如果此参数为false则程序发生异常
		log.info("--验证角色编码是否唯一---id:"+id+"--roleCode:"+roleCode);
		try {
			SysRole role = null;
			if(StringUtils.isNotBlank(id)) {
				role = sysRoleService.getById(id);
			}
			SysRole newRole = sysRoleService.getOne(new QueryWrapper<SysRole>().lambda().eq(SysRole::getRoleCode, roleCode));
			if(newRole!=null) {
				//如果根据传入的roleCode查询到信息了，那么就需要做校验了。
				if(role==null) {
					//role为空=>新增模式=>只要roleCode存在则返回false
					result.setSuccess(false);
					result.setMessage("角色编码已存在");
					return result;
				}else if(!id.equals(newRole.getId())) {
					//否则=>编辑模式=>判断两者ID是否一致-
					result.setSuccess(false);
					result.setMessage("角色编码已存在");
					return result;
				}
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(false);
			result.setMessage(e.getMessage());
			return result;
		}
		result.setSuccess(true);
		return result;
	}

//	/**
//	 * 导出excel
//	 * @param request
//	 */
//	@RequestMapping(value = "/exportXls")
//	public ModelAndView exportXls(SysRole sysRole, HttpServletRequest request) {
//		// Step.1 组装查询条件
//		QueryWrapper<SysRole> queryWrapper = QueryGenerator.initQueryWrapper(sysRole, request.getParameterMap());
//		//Step.2 AutoPoi 导出Excel
//		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
//		List<SysRole> pageList = sysRoleService.list(queryWrapper);
//		//导出文件名称
//		mv.addObject(NormalExcelConstants.FILE_NAME,"角色列表");
//		mv.addObject(NormalExcelConstants.CLASS,SysRole.class);
//		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//		mv.addObject(NormalExcelConstants.PARAMS,new ExportParams("角色列表数据","导出人:"+user.getRealname(),"导出信息"));
//		mv.addObject(NormalExcelConstants.DATA_LIST,pageList);
//		return mv;
//	}

	/**
	 * 通过excel导入数据
	 * @param request
	 * @param response
	 * @return
	 */
//	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
//	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
//		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
//		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
//			MultipartFile file = entity.getValue();// 获取上传文件对象
//			ImportParams params = new ImportParams();
//			params.setTitleRows(2);
//			params.setHeadRows(1);
//			params.setNeedSave(true);
//			try {
//				return sysRoleService.importExcelCheckRoleCode(file, params);
//			} catch (Exception e) {
//				log.error(e.getMessage(), e);
//				return Result.error("文件导入失败:" + e.getMessage());
//			} finally {
//				try {
//					file.getInputStream().close();
//				} catch (IOException e) {
//					log.error(e.getMessage(), e);
//				}
//			}
//		}
//		return Result.error("文件导入失败！");
//	}

//	/**
//	 * 查询数据规则数据
//	 */
//	@GetMapping(value = "/datarule/{permissionId}/{roleId}")
//	public Result<?> loadDatarule(@PathVariable("permissionId") String permissionId,@PathVariable("roleId") String roleId) {
//		List<SysPermissionDataRule> list = sysPermissionDataRuleService.getPermRuleListByPermId(permissionId);
//		if(list==null || list.size()==0) {
//			return Result.error("未找到权限配置信息");
//		}else {
//			Map<String,Object> map = new HashMap<>();
//			map.put("datarule", list);
//			LambdaQueryWrapper<SysRolePermission> query = new LambdaQueryWrapper<SysRolePermission>()
//					.eq(SysRolePermission::getPermissionId, permissionId)
//					.isNotNull(SysRolePermission::getDataRuleIds)
//					.eq(SysRolePermission::getRoleId,roleId);
//			SysRolePermission sysRolePermission = sysRolePermissionService.getOne(query);
//			if(sysRolePermission==null) {
//				//return Result.error("未找到角色菜单配置信息");
//			}else {
//				String drChecked = sysRolePermission.getDataRuleIds();
//				if(oConvertUtils.isNotEmpty(drChecked)) {
//					map.put("drChecked", drChecked.endsWith(",")?drChecked.substring(0, drChecked.length()-1):drChecked);
//				}
//			}
//			return Result.ok(map);
//			//TODO 以后按钮权限的查询也走这个请求 无非在map中多加两个key
//		}
//	}
//
//	/**
//	 * 保存数据规则至角色菜单关联表
//	 */
//	@PostMapping(value = "/datarule")
//	public Result<?> saveDatarule(@RequestBody JSONObject jsonObject) {
//		try {
//			String permissionId = jsonObject.getString("permissionId");
//			String roleId = jsonObject.getString("roleId");
//			String dataRuleIds = jsonObject.getString("dataRuleIds");
//			log.info("保存数据规则>>"+"菜单ID:"+permissionId+"角色ID:"+ roleId+"数据权限ID:"+dataRuleIds);
//			LambdaQueryWrapper<SysRolePermission> query = new LambdaQueryWrapper<SysRolePermission>()
//					.eq(SysRolePermission::getPermissionId, permissionId)
//					.eq(SysRolePermission::getRoleId,roleId);
//			SysRolePermission sysRolePermission = sysRolePermissionService.getOne(query);
//			if(sysRolePermission==null) {
//				return Result.error("请先保存角色菜单权限!");
//			}else {
//				sysRolePermission.setDataRuleIds(dataRuleIds);
//				this.sysRolePermissionService.updateById(sysRolePermission);
//			}
//		} catch (Exception e) {
//			log.error("SysRoleController.saveDatarule()发生异常：" + e.getMessage(),e);
//			return Result.error("保存失败");
//		}
//		return Result.ok("保存成功!");
//	}


	/**
	 * 用户角色授权功能，查询菜单权限树
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
	public Result<Map<String,Object>> queryTreeList(HttpServletRequest request) {
		Result<Map<String,Object>> result = new Result<>();
		//全部权限ids
		List<Long> ids = new ArrayList<>();
		try {
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
			query.orderByAsc(SysPermission::getSortNo);
			List<SysPermission> list = sysPermissionService.list(query);
			for(SysPermission sysPer : list) {
				ids.add(sysPer.getId());
			}
			List<TreeModel> treeList = new ArrayList<>();
			getTreeModelList(treeList, list, null);
			Map<String,Object> resMap = new HashMap<String,Object>();
			resMap.put("treeList", treeList); //全部树节点数据
			resMap.put("ids", ids);//全部树ids
			result.setResult(resMap);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	private void getTreeModelList(List<TreeModel> treeList,List<SysPermission> metaList,TreeModel temp) {
		for (SysPermission permission : metaList) {
			Long tempPid = permission.getParentId();
			TreeModel tree = new TreeModel(permission.getId(), tempPid, permission.getName(),permission.getRuleFlag(), permission.isLeaf());
			if(temp==null && tempPid == -1) {
				treeList.add(tree);
				if(!tree.getIsLeaf()) {
					getTreeModelList(treeList, metaList, tree);
				}
			}else if(temp!=null && tempPid!=null && tempPid.equals(temp.getKey())){
				temp.getChildren().add(tree);
				if(!tree.getIsLeaf()) {
					getTreeModelList(treeList, metaList, tree);
				}
			}

		}
	}


}
