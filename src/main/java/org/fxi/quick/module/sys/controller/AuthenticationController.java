package org.fxi.quick.module.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.fxi.quick.common.api.vo.Result;
import org.fxi.quick.common.constant.CommonConstant;
import org.fxi.quick.common.exception.BizException;
import org.fxi.quick.common.util.PasswordUtil;
import org.fxi.quick.common.vo.AccountContext;
import org.fxi.quick.module.sys.convert.SysDepartConverter;
import org.fxi.quick.module.sys.convert.SysUserConverter;
import org.fxi.quick.module.sys.entity.SysDepart;
import org.fxi.quick.module.sys.model.LoginUserModel;
import org.fxi.quick.module.sys.model.SysLoginModel;
import org.fxi.quick.module.sys.service.ISysDepartService;
import org.fxi.quick.module.sys.service.ISysUserService;
import org.fxi.quick.securty.jwt.JwtUtil;
import org.fxi.quick.module.sys.entity.SysUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CrossOrigin <= /sys/login 不在JWTFilter prehandler处理
 *
 * @author initializer
 * @date 2018-12-01 10:47
 */
@Api(description = "系统登录API")
@Slf4j
@RestController
@CrossOrigin
public class AuthenticationController {

  @Resource
  private ISysUserService sysUserService;

  @Resource
  private ISysDepartService sysDepartService;

  /**
   * 用户登录
   */
  @ApiOperation(value = "登录", produces = "application/json")
  @PostMapping(value = "/sys/login")
  public Result<LoginUserModel> login(@RequestBody SysLoginModel sysLoginModel) {
    SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().lambda()
        .eq(SysUser::getUsername, sysLoginModel.getUsername()));
    if (user == null || !StringUtils
        .equals(PasswordUtil
                .encrypt(sysLoginModel.getUsername(), sysLoginModel.getPassword(), user.getSalt()),
            user.getPassword())) {
      throw new BizException("A0201");
    }
    sysUserService.checkUserIsEffective(user);
    return userInfo(user);
  }

  /**
   * 测试
   */
  @ApiOperation(value = "权限测试")
  @RequiresPermissions("user:add")
  @PostMapping(value = "/sys/test")
  public Result test() {
    return Result.ok("");
  }


  /**
   * 用户信息
   */
  private Result<LoginUserModel> userInfo(SysUser sysUser) {
    Result<LoginUserModel> result = new Result<>();
    // 生成token
    String token = JwtUtil.createToken(
        new AccountContext(sysUser.getId(), sysUser.getUsername(), sysUser.getUserIdentity()));
    LoginUserModel loginUserModel = new LoginUserModel();
    // 获取用户部门信息
    List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
    loginUserModel.setDeparts(SysDepartConverter.INSTANCE.convertToModel(departs));
    loginUserModel.setMultiDepart(Math.max(departs.size(), 2));
    loginUserModel.setToken(token);
    loginUserModel.setUserInfo(SysUserConverter.INSTANCE.convertToModel(sysUser));
    //TODO  sysAllDictItems
    result.setResult(loginUserModel);
    result.success("登录成功");
    return result;
  }

  /**
   * 退出登录
   */
  @PostMapping(value = "/sys/logout")
  @ApiOperation(value = "退出登录", produces = "application/json")
  public Result<Object> logout(HttpServletRequest request, HttpServletResponse response) {
    return Result.ok("退出登录成功！");
  }

  @RequestMapping("/sys/common/401")
  public Result<?> handler401(HttpServletRequest request) {
    Object msg = request.getSession().getAttribute("msg");
    request.getSession().removeAttribute("msg");
    return Result.error(CommonConstant.RESPONSE_CODE_401, msg == null ? "未登录" : msg
        .toString());
  }
}
