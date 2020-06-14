package org.fxi.quick.sys.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 登录表单
 *
 * @Author scott
 * @since 2019-01-18
 */
@ApiModel(value = "登录成功返回对象", description = "登录成功返回对象")
@Data
public class LoginUserModel {

  @ApiModelProperty(value = "登录TOKEN")
  private String token;

  @ApiModelProperty(value = "用户信息")
  private SysUserModel userInfo;

  @ApiModelProperty(value = "部门数量")
  private Integer multiDepart;

  @ApiModelProperty(value = "部门信息")
  private List<SysDepartModel> departs;
}