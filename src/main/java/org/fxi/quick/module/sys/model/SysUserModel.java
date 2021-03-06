package org.fxi.quick.module.sys.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.fxi.quick.common.annotation.Dict;
import org.fxi.quick.common.constant.DateFormat;
import org.fxi.quick.common.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserModel  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 登录账号
     */
    private String username;

    /**
     * 登录账号
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    /**
     * 性别（1：男 2：女）
     */
    @Dict(dicCode = "sex")
    private Integer sex;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 部门code(当前选择登录部门)
     */
    private String orgCode;

    /**
     * 状态(1：正常  2：冻结 ）
     */
    @Dict(dicCode = "user_status")
    private Integer status;


    /**
     * 工号，唯一键
     */
    private String workNo;

    /**
     * 职务，关联职务表
     */
    private String post;

    /**
     * 座机号
     */
    private String telephone;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 同步工作流引擎1同步0不同步
     */
    private Integer activitiSync;

    /**
     * 身份（0 普通成员 1 上级）
     */
    private Integer userIdentity;

    /**
     * 负责部门
     */
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    private String departIds;

    /**
     * 选择的角色
     */
    private String selectedroles;

    /**
     * 选择的部门
     */
    private String selecteddeparts;

}
