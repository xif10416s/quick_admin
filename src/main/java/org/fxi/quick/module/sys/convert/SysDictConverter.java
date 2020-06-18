package org.fxi.quick.module.sys.convert;

import java.util.List;
import org.fxi.quick.module.sys.entity.SysDict;
import org.fxi.quick.module.sys.model.SysDictModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author wuzhengguo
 * @date 2020/2/10 14:33
 */
@Mapper(componentModel = "spring")
public interface SysDictConverter {

    SysDictConverter INSTANCE = Mappers.getMapper(SysDictConverter.class);

    /**
     * 转换成 Entity 对象
     * @param s
     * @return
     */
    SysDict convertToEntity(SysDictModel s);

    /**
     * 转换成 Entity 对象
     *
     * @param s
     * @return
     */
    List<SysDict> convertToEntity(List<SysDictModel> s);

    /**
     * 转换成 Model对象
     * @param s
     * @return
     */
    SysDictModel convertToModel(SysDict s);

    /**
     * 转换成 Model 对象
     *
     * @param s
     * @return
     */
    List<SysDictModel> convertToModel(List<SysDict> s);
}
