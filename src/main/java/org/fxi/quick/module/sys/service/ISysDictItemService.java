package org.fxi.quick.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.fxi.quick.module.sys.entity.SysDictItem;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface ISysDictItemService extends IService<SysDictItem> {
    public List<SysDictItem> selectItemsByMainId(String mainId);
}
