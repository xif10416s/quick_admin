package org.fxi.quick.module.sys.rule;

import static org.fxi.quick.module.sys.service.ISysCategoryService.ROOT_PID_VALUE;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.List;
import org.fxi.quick.module.sys.entity.SysCategory;
import org.fxi.quick.module.sys.mapper.SysCategoryMapper;
import org.fxi.quick.module.sys.util.ParentIdUtils;
import org.fxi.quick.module.sys.util.YouBianCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author scott
 * @Date 2019/12/9 11:32
 * @Description: 分类字典编码生成规则
 */
@Component
public class CategoryCodeRule {

    @Autowired
    private SysCategoryMapper sysCategoryMapper;

    public Object execute(JSONObject params) {

        Long categoryPid = ParentIdUtils.EMPTY_ID;
        String categoryCode = null;
        if (params != null) {
            Object obj = params.get("pid");
            if (obj != null){
                categoryPid = Long.valueOf(obj.toString());
            }
        }


        /*
         * 分成三种情况
         * 1.数据库无数据 调用YouBianCodeUtil.getNextYouBianCode(null);
         * 2.添加子节点，无兄弟元素 YouBianCodeUtil.getSubYouBianCode(parentCode,null);
         * 3.添加子节点有兄弟元素 YouBianCodeUtil.getNextYouBianCode(lastCode);
         * */
        //找同类 确定上一个最大的code值
        LambdaQueryWrapper<SysCategory> query = new LambdaQueryWrapper<SysCategory>().eq(SysCategory::getPid, categoryPid).isNotNull(SysCategory::getCode).orderByDesc(SysCategory::getCode);
        List<SysCategory> list = sysCategoryMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (ROOT_PID_VALUE.equals(categoryPid)) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                SysCategory parent = (SysCategory) sysCategoryMapper.selectById(categoryPid);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }
}
