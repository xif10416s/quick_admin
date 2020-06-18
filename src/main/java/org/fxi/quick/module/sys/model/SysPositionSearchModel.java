package org.fxi.quick.module.sys.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.fxi.quick.common.annotation.Dict;
import org.fxi.quick.common.model.PageSearchModel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 职务表
 * @Author: jeecg-boot
 * @Date: 2019-09-19
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "职务查询", description = "职务查询")
public class SysPositionSearchModel extends PageSearchModel {

    /**
     * 职务名称
     */
    @ApiModelProperty(value = "职务名称")
    private String name;

}
