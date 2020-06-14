package org.fxi.quick.sys.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.fxi.quick.securty.jwt.JwtUtil;
import org.fxi.quick.sys.entity.SysUser;
import org.fxi.quick.sys.model.SysLoginModel;
import org.fxi.quick.sys.service.ISysUserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author initializer
 * @date 2018-12-01 10:47
 */
@Api(description = "系统登录API")
@Slf4j
@RestController
@CrossOrigin
public class AuthenticationController {

    @Resource
    private ISysUserService userService;

    /**
     * 登录
     * @param sysLoginModel
     * @return
     */
    @ApiOperation(value = "登录", produces="application/json")
    @PostMapping(value = "/sys/login")
    public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel){

        SysUser user = userService.getOne(new QueryWrapper<SysUser>().lambda()
            .eq(SysUser::getUsername, sysLoginModel.getUsername()));

        // 用户名或密码错误
        if (user == null || !StringUtils
            .equals(PasswordUtil.encrypt(sysLoginModel.getUsername(), sysLoginModel.getPassword(), user.getSalt()),
                user.getPassword())) {
            throw new BizException("400204");
        }

        userService.checkUserIsEffective(user);

        return userInfo(user);
    }

    /**
     * 测试
     * @return
     */
    @ApiOperation(value = "权限测试")
    @RequiresPermissions("user:add")
    @PostMapping(value = "/sys/test")
    public Result test(){
        return Result.ok("");
    }


    /**
     * 用户信息
     *
     * @param sysUser
     * @return
     */
    private Result<JSONObject> userInfo(SysUser sysUser) {
        Result<JSONObject> result = new Result<JSONObject>();
        // 生成token
        String token = JwtUtil.createToken(
            new AccountContext(sysUser.getId(), sysUser.getUsername(), sysUser.getUserIdentity()));


        // 获取用户部门信息
        JSONObject obj = new JSONObject();
//        List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
//        obj.put("departs", departs);
//        if (departs == null || departs.size() == 0) {
//            obj.put("multi_depart", 0);
//        } else if (departs.size() == 1) {
//            sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
//            obj.put("multi_depart", 1);
//        } else {
//            obj.put("multi_depart", 2);
//        }
        obj.put("token", token);
        obj.put("userInfo", sysUser);
//        obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/sys/logout")
    public Result<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        return Result.ok("退出登录成功！");
    }

    @RequestMapping("/sys/common/401")
    public Result<?> handler401(HttpServletRequest request) {
        return Result.error(CommonConstant.RESPONSE_CODE_401,"未登录");
    }
}
