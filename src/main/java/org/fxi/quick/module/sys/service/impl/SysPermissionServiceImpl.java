package org.fxi.quick.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.fxi.quick.common.constant.CommonConstant;
import org.fxi.quick.common.exception.BizException;
import org.fxi.quick.module.sys.entity.SysPermission;
import org.fxi.quick.module.sys.mapper.SysPermissionMapper;
import org.fxi.quick.module.sys.mapper.SysRolePermissionMapper;
import org.fxi.quick.module.sys.service.ISysPermissionService;
import org.fxi.quick.module.sys.util.ParentIdUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SysPermissionServiceImpl  extends
    ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

  @Resource
  private SysRolePermissionMapper sysRolePermissionMapper;


  @Override
  public List<SysPermission> queryByUserId(Long userId) {
    List<SysPermission> permissionList = this.getBaseMapper().queryByUserId(userId);
    return permissionList;
  }

  @Override
  public void addPermission(SysPermission sysPermission) throws BizException {
    //----------------------------------------------------------------------
    //判断是否是一级菜单，是的话清空父菜单
    if(CommonConstant.MENU_TYPE_0.equals(sysPermission.getMenuType())) {
      sysPermission.setParentId(null);
    }
    //----------------------------------------------------------------------
    Long pid = sysPermission.getParentId();
    if( ParentIdUtils.isNotEmpty(pid)) {
      //设置父节点不为叶子节点
      this.getBaseMapper().setMenuLeaf(pid, 0);
    }
    sysPermission.setCreateTime(LocalDateTime.now());
    sysPermission.setDelFlag(Short.valueOf("0"));
    sysPermission.setLeaf(true);
    this.save(sysPermission);
  }

  @Override
  public void editPermission(SysPermission sysPermission) throws BizException {
    SysPermission oldPermission = this.getById(sysPermission.getId());
    //TODO 该节点判断是否还有子节点
    if(oldPermission ==null) {
      throw new BizException("未找到菜单信息");
    }else {
      sysPermission.setUpdateTime(LocalDateTime.now());
      //----------------------------------------------------------------------
      //Step1.判断是否是一级菜单，是的话清空父菜单ID
      if(CommonConstant.MENU_TYPE_0.equals(sysPermission.getMenuType())) {
        sysPermission.setParentId(ParentIdUtils.EMPTY_ID);
      }
      //Step2.判断菜单下级是否有菜单，无则设置为叶子节点
      int count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, sysPermission.getId()));
      if(count==0) {
        sysPermission.setLeaf(true);
      }
      //----------------------------------------------------------------------
      this.updateById(sysPermission);

      //如果当前菜单的父菜单变了，则需要修改新父菜单和老父菜单的，叶子节点状态
      Long pid = sysPermission.getParentId();
      Long oldParentId = oldPermission.getParentId();
      if((ParentIdUtils.isNotEmpty(pid) &&
          !pid.equals(oldParentId)) ||
          ParentIdUtils.isEmpty(pid) && ParentIdUtils.isNotEmpty(oldParentId)) {
        //a.设置新的父菜单不为叶子节点
        this.getBaseMapper().setMenuLeaf(pid, 0);
        //b.判断老的菜单下是否还有其他子菜单，没有的话则设置为叶子节点
        int cc = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, oldParentId));
        if(cc==0) {
          if(ParentIdUtils.isNotEmpty(oldParentId)) {
            this.getBaseMapper().setMenuLeaf(oldParentId, 1);
          }
        }

      }
    }
  }

  @Override
  @Transactional
  public void deletePermission(Long id) throws BizException {
    SysPermission sysPermission = this.getById(id);
    if(sysPermission==null) {
      throw new BizException("未找到菜单信息");
    }
    Long pid = sysPermission.getParentId();
    if(ParentIdUtils.isNotEmpty(pid)) {
      int count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, pid));
      if(count==1) {
        //若父节点无其他子节点，则该父节点是叶子节点
        this.getBaseMapper().setMenuLeaf(pid, 1);
      }
    }
    getBaseMapper().deleteById(id);
    // 该节点可能是子节点但也可能是其它节点的父节点,所以需要级联删除
    this.removeChildrenBy(sysPermission.getId());
    //关联删除
    Map map = new HashMap<>();
    map.put("permission_id",id);
    //删除角色授权表
    sysRolePermissionMapper.deleteByMap(map);
  }

  /**
   * 根据父id删除其关联的子节点数据
   *
   * @return
   */
  public void removeChildrenBy(Long parentId) {
    LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
    // 封装查询条件parentId为主键,
    query.eq(SysPermission::getParentId, parentId);
    // 查出该主键下的所有子级
    List<SysPermission> permissionList = this.list(query);
    if (permissionList != null && permissionList.size() > 0) {
      Long id = ParentIdUtils.EMPTY_ID; // id
      int num = 0; // 查出的子级数量
      // 如果查出的集合不为空, 则先删除所有
      this.remove(query);
      // 再遍历刚才查出的集合, 根据每个对象,查找其是否仍有子级
      for (int i = 0, len = permissionList.size(); i < len; i++) {
        id = permissionList.get(i).getId();
        Map map = new HashMap<>();
        map.put("permission_id",id);

        //删除角色授权表
        sysRolePermissionMapper.deleteByMap(map);
        num = this.count(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getParentId, id));
        // 如果有, 则递归
        if (num > 0) {
          this.removeChildrenBy(id);
        }
      }
    }
  }
}
