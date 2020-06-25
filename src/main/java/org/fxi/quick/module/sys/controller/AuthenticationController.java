package org.fxi.quick.module.sys.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.fxi.quick.common.api.vo.Result;
import org.fxi.quick.common.constant.CommonConstant;
import org.fxi.quick.common.exception.BizException;
import org.fxi.quick.common.util.PasswordUtil;
import org.fxi.quick.common.vo.AccountContext;
import org.fxi.quick.module.sys.convert.SysDepartConverter;
import org.fxi.quick.module.sys.convert.SysUserConverter;
import org.fxi.quick.module.sys.entity.SysDepart;
import org.fxi.quick.module.sys.entity.SysUser;
import org.fxi.quick.module.sys.model.LoginUserModel;
import org.fxi.quick.module.sys.model.SysLoginModel;
import org.fxi.quick.module.sys.service.ISysDepartService;
import org.fxi.quick.module.sys.service.ISysUserService;
import org.fxi.quick.module.sys.util.RandImageUtil;
import org.fxi.quick.securty.jwt.JwtUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

  /**
   * 发送短信验证码
   * TODO
   * @param jsonObject
   * @return
   */
  @PostMapping(value = "/sms")
  public Result<String> sms(@RequestBody JSONObject jsonObject) {
    Result<String> result = new Result<String>();
    String mobile = jsonObject.get("mobile").toString();
    //手机号模式 登录模式: "2"  注册模式: "1"
    String smsmode=jsonObject.get("smsmode").toString();
    log.info(mobile);
    if(StringUtils.isEmpty(mobile)){
      result.setMessage("手机号不允许为空！");
      result.setSuccess(false);
      return result;
    }
//    Object object = redisUtil.get(mobile);
//    if (object != null) {
//      result.setMessage("验证码10分钟内，仍然有效！");
//      result.setSuccess(false);
//      return result;
//    }

    //随机数
    String captcha = RandomUtil.randomNumbers(6);
    JSONObject obj = new JSONObject();
    obj.put("code", captcha);
//    try {
//      boolean b = false;
//      //注册模板
//      if (CommonConstant.SMS_TPL_TYPE_1.equals(smsmode)) {
//        SysUser sysUser = sysUserService.getUserByPhone(mobile);
//        if(sysUser!=null) {
//          result.error500(" 手机号已经注册，请直接登录！");
//          return result;
//        }
//        b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.REGISTER_TEMPLATE_CODE);
//      }else {
//        //登录模式，校验用户有效性
//        SysUser sysUser = sysUserService.getUserByPhone(mobile);
//        result = sysUserService.checkUserIsEffective(sysUser);
//        if(!result.isSuccess()) {
//          return result;
//        }
//
//        /**
//         * smsmode 短信模板方式  0 .登录模板、1.注册模板、2.忘记密码模板
//         */
//        if (CommonConstant.SMS_TPL_TYPE_0.equals(smsmode)) {
//          //登录模板
//          b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.LOGIN_TEMPLATE_CODE);
//        } else if(CommonConstant.SMS_TPL_TYPE_2.equals(smsmode)) {
//          //忘记密码模板
//          b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.FORGET_PASSWORD_TEMPLATE_CODE);
//        }
//      }
//
//      if (b == false) {
//        result.setMessage("短信验证码发送失败,请稍后重试");
//        result.setSuccess(false);
//        return result;
//      }
//      //验证码10分钟内有效
//      redisUtil.set(mobile, captcha, 600);
//      //update-begin--Author:scott  Date:20190812 for：issues#391
//      //result.setResult(captcha);
//      //update-end--Author:scott  Date:20190812 for：issues#391
//      result.setSuccess(true);
//
//    } catch (ClientException e) {
//      e.printStackTrace();
//      result.error500(" 短信接口未配置，请联系管理员！");
//      return result;
//    }
    return result;
  }

  private static final String BASE_CHECK_CODES = "q2sdwertyuiplkjhgfdsazxcvbnmddQWERTYUPLKJHGFDSAZXCVBNM1234567890";

  /**
   * 后台生成图形验证码 ：有效
   * @param response
   * @param key
   */
  @ApiOperation("获取验证码")
  @GetMapping(value = "/randomImage/{key}")
  public Result<String> randomImage(HttpServletResponse response,@PathVariable String key){
    Result<String> res = new Result<String>();
    try {
      String code = RandomUtil.randomString(BASE_CHECK_CODES,4);
      String lowerCaseCode = code.toLowerCase();
      String realKey =  DigestUtil.md5Hex(lowerCaseCode+key, "utf-8");
//      redisUtil.set(realKey, lowerCaseCode, 60); //TODO
      String base64 = RandImageUtil.generate(code);
      res.setSuccess(true);
      res.setResult(base64);
    } catch (Exception e) {
      res.error500("获取验证码出错"+e.getMessage());
      e.printStackTrace();
    }
    return res;
  }

  /**
   * 图形验证码
   * TODO
   * @param sysLoginModel
   * @return
   */
  @RequestMapping(value = "/checkCaptcha", method = RequestMethod.POST)
  public Result<?> checkCaptcha(@RequestBody SysLoginModel sysLoginModel){
    String captcha = sysLoginModel.getCaptcha();
    String checkKey = sysLoginModel.getCheckKey();
    if(captcha==null){
      return Result.error("验证码无效");
    }
    String lowerCaseCaptcha = captcha.toLowerCase();
    String realKey = DigestUtil.md5Hex(lowerCaseCaptcha+checkKey, "utf-8");
//    Object checkCode = redisUtil.get(realKey);
//    if(checkCode==null || !checkCode.equals(lowerCaseCaptcha)) {
//      return Result.error("验证码错误");
//    }
    return Result.ok();
  }

  /**
   * 登陆成功选择用户当前部门
   * @param user
   * @return
   */
  @RequestMapping(value = "/selectDepart", method = RequestMethod.PUT)
  public Result<JSONObject> selectDepart(@RequestBody SysUser user) {
    Result<JSONObject> result = new Result<JSONObject>();
    String username = user.getUsername();
    if(StringUtils.isEmpty(username)) {
      SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
      username = sysUser.getUsername();
    }
    String orgCode= user.getOrgCode();
    this.sysUserService.updateUserDepart(username, orgCode);
    SysUser sysUser = sysUserService.getUserByName(username);
    JSONObject obj = new JSONObject();
    obj.put("userInfo", sysUser);
    result.setResult(obj);
    return result;
  }
}
