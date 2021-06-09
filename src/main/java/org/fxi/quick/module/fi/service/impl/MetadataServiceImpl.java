package org.fxi.quick.module.fi.service.impl;

import org.fxi.quick.module.fi.entity.Metadata;
import org.fxi.quick.module.fi.mapper.MetadataMapper;
import org.fxi.quick.module.fi.service.IMetadataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 金融基础设施-元数据信息表 服务实现类
 * </p>
 *
 * @author initializer
 * @since 2021-01-22
 */
@Service
public class MetadataServiceImpl extends ServiceImpl<MetadataMapper, Metadata> implements IMetadataService {

}
