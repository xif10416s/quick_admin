package org.fxi.quick.module.sys.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.fxi.quick.common.constant.DateFormat;
import org.fxi.quick.common.entity.BaseEntity;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRoleModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
          * 描述
     */
    private String description;

    /**
     * 创建人
     */
    private String createBy;


    /**
     * 更新人
     */
    private String updateBy;


    /**
     * 更新人
     */
    @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime createTime;
}
