package org.fxi.quick.module.oa.convert;

import java.util.List;
import org.fxi.quick.module.oa.entity.LeaveApply;
import org.fxi.quick.module.oa.model.LeaveApplyModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author wuzhengguo
 * @date 2020/2/10 14:33
 */
@Mapper(componentModel = "spring")
public interface LeaveApplyConverter {

    LeaveApplyConverter INSTANCE = Mappers.getMapper(LeaveApplyConverter.class);

    /**
     * 转换成 Entity 对象
     * @param s
     * @return
     */
    LeaveApply convertToEntity(LeaveApplyModel s);

    /**
     * 转换成 Entity 对象
     *
     * @param s
     * @return
     */
    List<LeaveApply> convertToEntity(List<LeaveApplyModel> s);

    /**
     * 转换成 Model对象
     * @param s
     * @return
     */
    LeaveApplyModel convertToModel(LeaveApply s);

    /**
     * 转换成 Model 对象
     *
     * @param s
     * @return
     */
    List<LeaveApplyModel> convertToModel(List<LeaveApply> s);
}
