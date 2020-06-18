package org.fxi.quick.module.sys.convert;

import java.util.List;
import org.fxi.quick.module.sys.entity.SysRole;
import org.fxi.quick.module.sys.model.SysRoleModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author wuzhengguo
 * @date 2020/2/10 14:33
 */
@Mapper(componentModel = "spring")
public interface SysRoleConverter {

    SysRoleConverter INSTANCE = Mappers.getMapper(SysRoleConverter.class);

    /**
     * 转换成 Entity 对象
     * @param s
     * @return
     */
    SysRole convertToEntity(SysRoleModel s);

    /**
     * 转换成 Entity 对象
     *
     * @param s
     * @return
     */
    List<SysRole> convertToEntity(List<SysRoleModel> s);

    /**
     * 转换成 Model对象
     * @param s
     * @return
     */
    SysRoleModel convertToModel(SysRole s);

    /**
     * 转换成 Model 对象
     *
     * @param s
     * @return
     */
    List<SysRoleModel> convertToModel(List<SysRole> s);
}
