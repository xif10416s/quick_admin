package org.fxi.quick.module.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.fxi.quick.common.api.vo.Result;
import org.fxi.quick.common.constant.CacheConstant;
import org.fxi.quick.module.sys.convert.SysDictItemConverter;
import org.fxi.quick.module.sys.entity.SysDictItem;
import org.fxi.quick.module.sys.model.SysDictItemModel;
import org.fxi.quick.module.sys.service.ISysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@RequestMapping("/sys/dictItem")
@Api(description = "字典项接口")
@Slf4j
@RestController
public class SysDictItemController {

	@Autowired
	private ISysDictItemService sysDictItemService;

	/**
	 * @功能：查询字典数据
	 * @param sysDictItem
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/list")
	public Result<IPage<SysDictItemModel>> queryPageList(@RequestBody  SysDictItemModel sysDictItem) {
		Result<IPage<SysDictItemModel>> result = new Result<IPage<SysDictItemModel>>();
		LambdaQueryWrapper<SysDictItem> queryWrapper = new QueryWrapper<SysDictItem>().lambda();
		if(sysDictItem.getDictId() != null){
			queryWrapper.eq(SysDictItem::getDictId,sysDictItem.getDictId());
		}

		if(StringUtils.isNotBlank(sysDictItem.getItemValue())){
			queryWrapper.eq(SysDictItem::getItemValue,sysDictItem.getItemValue());
		}

		if(StringUtils.isNotBlank(sysDictItem.getItemText())){
			queryWrapper.eq(SysDictItem::getItemText,sysDictItem.getItemText());
		}

		queryWrapper.last("order by sort_order desc");
		Page<SysDictItem> page = new Page<SysDictItem>(sysDictItem.getPageNo(), sysDictItem.getPageSize());
		IPage<SysDictItemModel> pageList = sysDictItemService.page(page, queryWrapper).convert(
				SysDictItemConverter.INSTANCE::convertToModel);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * @功能：新增
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@CacheEvict(value= CacheConstant.SYS_DICT_CACHE, allEntries=true)
	public Result<SysDictItem> add(@RequestBody SysDictItem sysDictItem) {
		Result<SysDictItem> result = new Result<SysDictItem>();
		try {
			sysDictItem.setCreateTime(LocalDateTime.now());
			sysDictItemService.save(sysDictItem);
			result.success("保存成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * @功能：编辑
	 * @param sysDictItem
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	@CacheEvict(value=CacheConstant.SYS_DICT_CACHE, allEntries=true)
	public Result<SysDictItem> edit(@RequestBody SysDictItem sysDictItem) {
		Result<SysDictItem> result = new Result<SysDictItem>();
		SysDictItem sysdict = sysDictItemService.getById(sysDictItem.getId());
		if(sysdict==null) {
			result.error500("未找到对应实体");
		}else {
			sysDictItem.setUpdateTime(LocalDateTime.now());
			boolean ok = sysDictItemService.updateById(sysDictItem);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("编辑成功!");
			}
		}
		return result;
	}

	/**
	 * @功能：删除字典数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@CacheEvict(value=CacheConstant.SYS_DICT_CACHE, allEntries=true)
	public Result<SysDictItem> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysDictItem> result = new Result<SysDictItem>();
		SysDictItem joinSystem = sysDictItemService.getById(id);
		if(joinSystem==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysDictItemService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		return result;
	}

	/**
	 * @功能：批量删除字典数据
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	@CacheEvict(value=CacheConstant.SYS_DICT_CACHE, allEntries=true)
	public Result<SysDictItem> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysDictItem> result = new Result<SysDictItem>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysDictItemService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

}
