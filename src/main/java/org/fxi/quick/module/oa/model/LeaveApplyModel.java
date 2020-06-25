package org.fxi.quick.module.oa.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.fxi.quick.common.annotation.Dict;
import org.fxi.quick.common.constant.DateFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author initializer
 * @since 2020-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LeaveApplyModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String username;

    /**
     * 请假类型
     */
    @ApiModelProperty(value = "请假类型")
    @Dict(dicCode = "oa_leave_type")
    private Integer leaveType;

    /**
     * 原因
     */
    @ApiModelProperty(value = "请假原因")
    private String leaveReason;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = DateFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime startTime;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = DateFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime endTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = DateFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = DateFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;

}
