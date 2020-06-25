package org.fxi.quick.module.sys.model;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fxi.quick.common.model.PageSearchModel;

/**
 * <p>
 *
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDeptSearchModel extends PageSearchModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long depId;

    private String username;


}
