package org.fxi.quick.sys.convert;

import java.util.List;
import org.fxi.quick.sys.entity.SysUser;
import org.fxi.quick.sys.model.SysUserModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author wuzhengguo
 * @date 2020/2/10 14:33
 */
@Mapper(componentModel = "spring")
public interface SysUserConverter {

    SysUserConverter INSTANCE = Mappers.getMapper(SysUserConverter.class);

    /**
     * 转换成 Entity 对象
     * @param s
     * @return
     */
    SysUser convertToEntity(SysUserModel s);

    /**
     * 转换成 Entity 对象
     *
     * @param s
     * @return
     */
    List<SysUser> convertToEntity(List<SysUserModel> s);

    /**
     * 转换成 Model对象
     * @param s
     * @return
     */
    SysUserModel convertToModel(SysUser s);

    /**
     * 转换成 Model 对象
     *
     * @param s
     * @return
     */
    List<SysUserModel> convertToModel(List<SysUser> s);
}
