package org.fxi.quick.module.sys.convert;

import java.util.List;
import org.fxi.quick.module.sys.entity.SysAnnouncement;
import org.fxi.quick.module.sys.model.SysAnnouncementModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author wuzhengguo
 * @date 2020/2/10 14:33
 */
@Mapper(componentModel = "spring")
public interface SysAnnouncementConverter {

    SysAnnouncementConverter INSTANCE = Mappers.getMapper(SysAnnouncementConverter.class);

    /**
     * 转换成 Entity 对象
     * @param s
     * @return
     */
    SysAnnouncement convertToEntity(SysAnnouncementModel s);

    /**
     * 转换成 Entity 对象
     *
     * @param s
     * @return
     */
    List<SysAnnouncement> convertToEntity(List<SysAnnouncementModel> s);

    /**
     * 转换成 Model对象
     * @param s
     * @return
     */
    SysAnnouncementModel convertToModel(SysAnnouncement s);

    /**
     * 转换成 Model 对象
     *
     * @param s
     * @return
     */
    List<SysAnnouncementModel> convertToModel(List<SysAnnouncement> s);
}
