package org.fxi.quick.module.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.fxi.quick.common.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 职务表
 * @Author: jeecg-boot
 * @Date: 2019-09-19
 * @Version: V1.0
 */
@Data
@TableName("sys_position")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_position对象", description = "职务表")
public class SysPosition {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 职务编码
     */
    @ApiModelProperty(value = "职务编码")
    private String code;
    /**
     * 职务名称
     */
    @ApiModelProperty(value = "职务名称")
    private String name;

    /**
     * 职级
     */
    @ApiModelProperty(value = "职级")
    @Dict(dicCode = "position_rank")
    private String postRank;

    /**
     * 公司id
     */
    @ApiModelProperty(value = "公司id")
    private String companyId;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;
    /**
     * 组织机构编码
     */
    @ApiModelProperty(value = "组织机构编码")
    private String sysOrgCode;
}
