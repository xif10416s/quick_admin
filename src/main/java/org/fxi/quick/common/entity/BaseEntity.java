package org.fxi.quick.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fxi.quick.common.constant.DateFormat;

/**
 * 实体类基类
 *
 * @author initializer
 * @date 2018-12-02 11:03
 */
@Data
@EqualsAndHashCode
public class BaseEntity {

    /**
     * 物理主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime updateTime;



    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;

    @TableLogic
    private Integer delFlag;
}
