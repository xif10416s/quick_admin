package org.fxi.quick.module.sys.convert;

import java.util.List;
import org.fxi.quick.module.sys.entity.SysDictItem;
import org.fxi.quick.module.sys.model.SysDictItemModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author wuzhengguo
 * @date 2020/2/10 14:33
 */
@Mapper(componentModel = "spring")
public interface SysDictItemConverter {

    SysDictItemConverter INSTANCE = Mappers.getMapper(SysDictItemConverter.class);

    /**
     * 转换成 Entity 对象
     * @param s
     * @return
     */
    SysDictItem convertToEntity(SysDictItemModel s);

    /**
     * 转换成 Entity 对象
     *
     * @param s
     * @return
     */
    List<SysDictItem> convertToEntity(List<SysDictItemModel> s);

    /**
     * 转换成 Model对象
     * @param s
     * @return
     */
    SysDictItemModel convertToModel(SysDictItem s);

    /**
     * 转换成 Model 对象
     *
     * @param s
     * @return
     */
    List<SysDictItemModel> convertToModel(List<SysDictItem> s);
}
