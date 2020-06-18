package org.fxi.quick.module.sys.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fxi.quick.common.api.vo.Result;
import org.fxi.quick.common.constant.CommonConstant;
import org.fxi.quick.common.util.AccountContextUtil;
import org.fxi.quick.module.sys.entity.SysPermission;
import org.fxi.quick.module.sys.service.ISysPermissionService;
import org.fxi.quick.module.sys.util.PermissionDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Slf4j
@RestController
@RequestMapping("/sys/permission")
public class SysPermissionController {

	@Autowired
	private ISysPermissionService sysPermissionService;


	/**
	 * 查询用户拥有的菜单权限和按钮权限（根据TOKEN）
	 *
	 * @return
	 */
	@RequestMapping(value = "/getUserPermissionByToken", method = RequestMethod.GET)
	public Result<?> getUserPermissionByToken() {
		Result<JSONObject> result = new Result<JSONObject>();
    Long userId = AccountContextUtil.getAccountContext().getUserId();
    List<SysPermission> sysPermissions = sysPermissionService.queryByUserId(userId);
    if(!PermissionDataUtil.hasIndexPage(sysPermissions)){
      SysPermission main = sysPermissionService
          .getOne(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getName, "首页"));
      if(main != null){
        sysPermissions.add(0, main);
      }
    }
    //update-end-author:taoyan date:20200211 for: TASK #3368 【路由缓存】首页的缓存设置有问题，需要根据后台的路由配置来实现是否缓存
    JSONObject json = new JSONObject();
    JSONArray menujsonArray = new JSONArray();
    this.getPermissionJsonArray(menujsonArray, sysPermissions, null);
    JSONArray authjsonArray = new JSONArray();
    this.getAuthJsonArray(authjsonArray, sysPermissions);
    //查询所有的权限
    LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
    query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
    query.eq(SysPermission::getMenuType, CommonConstant.MENU_TYPE_2);
    //query.eq(SysPermission::getStatus, "1");
    List<SysPermission> allAuthList = sysPermissionService.list(query);
    JSONArray allauthjsonArray = new JSONArray();
    this.getAllAuthJsonArray(allauthjsonArray, allAuthList);
    //路由菜单
    json.put("menu", menujsonArray);
    //按钮权限（用户拥有的权限集合）
    json.put("auth", authjsonArray);
    //全部权限配置集合（按钮权限，访问权限）
    json.put("allAuth", allauthjsonArray);
    result.setResult(json);
    result.success("查询成功");
		return result;
	}

  /**
   *  获取权限JSON数组
   * @param jsonArray
   * @param allList
   */
  private void getAllAuthJsonArray(JSONArray jsonArray,List<SysPermission> allList) {
    JSONObject json = null;
    for (SysPermission permission : allList) {
      json = new JSONObject();
      json.put("action", permission.getPerms());
      json.put("status", permission.getStatus());
      json.put("type", permission.getPermsType());
      json.put("describe", permission.getName());
      jsonArray.add(json);
    }
  }

  /**
   *  获取权限JSON数组
   * @param jsonArray
   * @param metaList
   */
  private void getAuthJsonArray(JSONArray jsonArray,List<SysPermission> metaList) {
    for (SysPermission permission : metaList) {
      if(permission.getMenuType()==null) {
        continue;
      }
      JSONObject json = null;
      if(permission.getMenuType().equals(CommonConstant.MENU_TYPE_2) &&CommonConstant.STATUS_1.equals(permission.getStatus())) {
        json = new JSONObject();
        json.put("action", permission.getPerms());
        json.put("type", permission.getPermsType());
        json.put("describe", permission.getName());
        jsonArray.add(json);
      }
    }
  }
  /**
   *  获取菜单JSON数组
   * @param jsonArray
   * @param metaList
   * @param parentJson
   */
  private void getPermissionJsonArray(JSONArray jsonArray, List<SysPermission> metaList, JSONObject parentJson) {
    for (SysPermission permission : metaList) {
      if (permission.getMenuType() == null) {
        continue;
      }
      Long tempPid = permission.getParentId();
      JSONObject json = getPermissionJsonObject(permission);
      if(json==null) {
        continue;
      }
      if (parentJson == null && SysPermission.NO_PARENT_ID == tempPid) {
        jsonArray.add(json);
        if (!permission.isLeaf()) {
          getPermissionJsonArray(jsonArray, metaList, json);
        }
      } else if (parentJson != null && SysPermission.NO_PARENT_ID != tempPid && tempPid.equals(parentJson.getLong("id"))) {
        // 类型( 0：一级菜单 1：子菜单 2：按钮 )
        if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_2)) {
          JSONObject metaJson = parentJson.getJSONObject("meta");
          if (metaJson.containsKey("permissionList")) {
            metaJson.getJSONArray("permissionList").add(json);
          } else {
            JSONArray permissionList = new JSONArray();
            permissionList.add(json);
            metaJson.put("permissionList", permissionList);
          }
          // 类型( 0：一级菜单 1：子菜单 2：按钮 )
        } else if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_1) || permission.getMenuType().equals(CommonConstant.MENU_TYPE_0)) {
          if (parentJson.containsKey("children")) {
            parentJson.getJSONArray("children").add(json);
          } else {
            JSONArray children = new JSONArray();
            children.add(json);
            parentJson.put("children", children);
          }

          if (!permission.isLeaf()) {
            getPermissionJsonArray(jsonArray, metaList, json);
          }
        }
      }
    }
  }

  /**
   * 根据菜单配置生成路由json
   * @param permission
   * @return
   */
  private JSONObject getPermissionJsonObject(SysPermission permission) {
    JSONObject json = new JSONObject();
    // 类型(0：一级菜单 1：子菜单 2：按钮)
    if (CommonConstant.MENU_TYPE_2.equals(permission.getMenuType())) {
      return null;
    } else if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_0) || permission.getMenuType().equals(CommonConstant.MENU_TYPE_1)) {
      json.put("id", permission.getId());
      if (permission.isRoute()) {
        json.put("route", "1");// 表示生成路由
      } else {
        json.put("route", "0");// 表示不生成路由
      }

      if (isWWWHttpUrl(permission.getUrl())) {
        json.put("path", SecureUtil.md5(permission.getUrl()));
      } else {
        json.put("path", permission.getUrl());
      }

      // 重要规则：路由name (通过URL生成路由name,路由name供前端开发，页面跳转使用)
      if (StringUtils.isNotEmpty(permission.getComponentName())) {
        json.put("name", permission.getComponentName());
      } else {
        json.put("name", urlToRouteName(permission.getUrl()));
      }

      // 是否隐藏路由，默认都是显示的
      if (permission.isHidden()) {
        json.put("hidden", true);
      }
      // 聚合路由
      if (permission.isAlwaysShow()) {
        json.put("alwaysShow", true);
      }
      json.put("component", permission.getComponent());
      JSONObject meta = new JSONObject();
      // 由用户设置是否缓存页面 用布尔值
      if (permission.isKeepAlive()) {
        meta.put("keepAlive", true);
      } else {
        meta.put("keepAlive", false);
      }

      meta.put("title", permission.getName());
      if (SysPermission.NO_PARENT_ID == permission.getParentId()) {
        // 一级菜单跳转地址
        json.put("redirect", permission.getRedirect());
        if (StringUtils.isNotEmpty(permission.getIcon())) {
          meta.put("icon", permission.getIcon());
        }
      } else {
        if (StringUtils.isNotEmpty(permission.getIcon())) {
          meta.put("icon", permission.getIcon());
        }
      }
      if (isWWWHttpUrl(permission.getUrl())) {
        meta.put("url", permission.getUrl());
      }
      json.put("meta", meta);
    }

    return json;
  }

  /**
   * 判断是否外网URL 例如： http://localhost:8080/jeecg-boot/swagger-ui.html#/ 支持特殊格式： {{
   * window._CONFIG['domianURL'] }}/druid/ {{ JS代码片段 }}，前台解析会自动执行JS代码片段
   *
   * @return
   */
  private boolean isWWWHttpUrl(String url) {
    if (url != null && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("{{"))) {
      return true;
    }
    return false;
  }

  /**
   * 通过URL生成路由name（去掉URL前缀斜杠，替换内容中的斜杠‘/’为-） 举例： URL = /isystem/role RouteName =
   * isystem-role
   *
   * @return
   */
  private String urlToRouteName(String url) {
    if (StringUtils.isNotEmpty(url)) {
      if (url.startsWith("/")) {
        url = url.substring(1);
      }
      url = url.replace("/", "-");

      // 特殊标记
      url = url.replace(":", "@");
      return url;
    } else {
      return null;
    }
  }

}
