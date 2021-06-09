package org.fxi.quick.module.fi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 金融基础设施-元数据信息表
 * </p>
 *
 * @author initializer
 * @since 2021-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fi_metadata")
public class Metadata implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 采集接口标识符
     */
    private String dataType;

    /**
     * 采集接口名称
     */
    private String dataTypeName;

    /**
     * 全局唯一标识符
     */
    @TableField("facilityDescriptor")
    private String facilityDescriptor;

    /**
     * 同步状态：0 未同步，1 同步中 ， 2 同步失败 ， 3  已上传， 
     */
    private String status;

    /**
     * 元数据信息
     */
    private String json;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
