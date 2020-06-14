package org.fxi.quick.sys.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fxi.quick.common.entity.BaseEntity;
import org.fxi.quick.common.model.PageSearchModel;
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
public class SysUserSearchModel extends PageSearchModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录账号
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realname;




    /**
     * 性别（1：男 2：女）
     */
    private Integer sex;


    /**
     * 电话
     */
    private String phone;


    /**
     * 状态(1：正常  2：冻结 ）
     */
    private Integer status;
}
