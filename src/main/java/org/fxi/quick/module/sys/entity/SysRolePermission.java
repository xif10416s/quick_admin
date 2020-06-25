package org.fxi.quick.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.fxi.quick.common.entity.BaseEntity;

/**
 * <p>
 * 角色权限表
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

  /**
   * 物理主键
   */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限id
     */
    private Long permissionId;

    /**
     * 数据权限
     */
    private String dataRuleIds;

    public SysRolePermission() {
   	}

   	public SysRolePermission(Long roleId, Long permissionId) {
   		this.roleId = roleId;
   		this.permissionId = permissionId;
   	}

}
