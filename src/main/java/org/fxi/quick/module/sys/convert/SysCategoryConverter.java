package org.fxi.quick.module.sys.convert;

import java.util.List;
import org.fxi.quick.module.sys.entity.SysCategory;
import org.fxi.quick.module.sys.model.SysCategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author wuzhengguo
 * @date 2020/2/10 14:33
 */
@Mapper(componentModel = "spring")
public interface SysCategoryConverter {

    SysCategoryConverter INSTANCE = Mappers.getMapper(SysCategoryConverter.class);

    /**
     * 转换成 Entity 对象
     * @param s
     * @return
     */
    SysCategory convertToEntity(SysCategoryModel s);

    /**
     * 转换成 Entity 对象
     *
     * @param s
     * @return
     */
    List<SysCategory> convertToEntity(List<SysCategoryModel> s);

    /**
     * 转换成 Model对象
     * @param s
     * @return
     */
    SysCategoryModel convertToModel(SysCategory s);

    /**
     * 转换成 Model 对象
     *
     * @param s
     * @return
     */
    List<SysCategoryModel> convertToModel(List<SysCategory> s);
}
