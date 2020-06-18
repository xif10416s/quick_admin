package org.fxi.quick.module.sys.convert;

import java.util.List;
import org.fxi.quick.module.sys.entity.SysDepart;
import org.fxi.quick.module.sys.model.SysDepartModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author wuzhengguo
 * @date 2020/2/10 14:33
 */
@Mapper(componentModel = "spring")
public interface SysDepartConverter {

    SysDepartConverter INSTANCE = Mappers.getMapper(SysDepartConverter.class);

    /**
     * 转换成 Entity 对象
     * @param s
     * @return
     */
    SysDepart convertToEntity(SysDepartModel s);

    /**
     * 转换成 Entity 对象
     *
     * @param s
     * @return
     */
    List<SysDepart> convertToEntity(List<SysDepartModel> s);

    /**
     * 转换成 Model对象
     * @param s
     * @return
     */
    SysDepartModel convertToModel(SysDepart s);

    /**
     * 转换成 Model 对象
     *
     * @param s
     * @return
     */
    List<SysDepartModel> convertToModel(List<SysDepart> s);
}
