package org.fxi.quick.module.sys.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.fxi.quick.common.model.PageSearchModel;

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
public class SysRoleSearchModel extends PageSearchModel implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 角色名称
     */
    private String roleName;

}
