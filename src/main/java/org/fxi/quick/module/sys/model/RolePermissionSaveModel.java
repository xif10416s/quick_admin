package org.fxi.quick.module.sys.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.fxi.quick.common.constant.DateFormat;

/**
 * <p>
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RolePermissionSaveModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;

    private String permissionIds;

    private String lastpermissionIds;
}
