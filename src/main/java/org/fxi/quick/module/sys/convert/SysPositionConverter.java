package org.fxi.quick.module.sys.convert;

import java.util.List;
import org.fxi.quick.module.sys.entity.SysPosition;
import org.fxi.quick.module.sys.model.SysPositionModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author wuzhengguo
 * @date 2020/2/10 14:33
 */
@Mapper(componentModel = "spring")
public interface SysPositionConverter {

    SysPositionConverter INSTANCE = Mappers.getMapper(SysPositionConverter.class);

    /**
     * 转换成 Entity 对象
     * @param s
     * @return
     */
    SysPosition convertToEntity(SysPositionModel s);

    /**
     * 转换成 Entity 对象
     *
     * @param s
     * @return
     */
    List<SysPosition> convertToEntity(List<SysPositionModel> s);

    /**
     * 转换成 Model对象
     * @param s
     * @return
     */
    SysPositionModel convertToModel(SysPosition s);

    /**
     * 转换成 Model 对象
     *
     * @param s
     * @return
     */
    List<SysPositionModel> convertToModel(List<SysPosition> s);
}
