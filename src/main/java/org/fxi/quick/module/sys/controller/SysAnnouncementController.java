package org.fxi.quick.module.sys.controller;


import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.fxi.quick.common.api.vo.Result;
import org.fxi.quick.common.constant.CommonConstant;
import org.fxi.quick.common.constant.CommonSendStatus;
import org.fxi.quick.common.constant.WebsocketConst;
import org.fxi.quick.common.util.AccountContextUtil;
import org.fxi.quick.message.websocket.WebSocket;
import org.fxi.quick.module.sys.entity.SysAnnouncement;
import org.fxi.quick.module.sys.entity.SysAnnouncementSend;
import org.fxi.quick.module.sys.entity.SysUser;
import org.fxi.quick.module.sys.service.ISysAnnouncementSendService;
import org.fxi.quick.module.sys.service.ISysAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: Controller
 * @Description: 系统通告表
 * @Author: jeecg-boot
 * @Date: 2019-01-02
 * @Version: V1.0
 */
@RestController
@RequestMapping("/sys/annountCement")
@Slf4j
public class SysAnnouncementController {

  @Autowired
  private ISysAnnouncementService sysAnnouncementService;
  @Autowired
  private ISysAnnouncementSendService sysAnnouncementSendService;
  @Resource
  private WebSocket webSocket;

  /**
   * 分页列表查询
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public Result<IPage<SysAnnouncement>> queryPageList(SysAnnouncement sysAnnouncement,
      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
      HttpServletRequest req) {
    Result<IPage<SysAnnouncement>> result = new Result<IPage<SysAnnouncement>>();
    sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0);
    QueryWrapper<SysAnnouncement> queryWrapper = new QueryWrapper<SysAnnouncement>(sysAnnouncement);
    Page<SysAnnouncement> page = new Page<SysAnnouncement>(pageNo, pageSize);
    //排序逻辑 处理
    String column = req.getParameter("column");
    String order = req.getParameter("order");
    if (StringUtils.isNotBlank(column) && StringUtils.isBlank(order)) {
      if ("asc".equals(order)) {
        queryWrapper.orderByAsc(StringUtils.camelToUnderline(column));
      } else {
        queryWrapper.orderByDesc(StringUtils.camelToUnderline(column));
      }
    }
    IPage<SysAnnouncement> pageList = sysAnnouncementService.page(page, queryWrapper);
    log.info("查询当前页：" + pageList.getCurrent());
    log.info("查询当前页数量：" + pageList.getSize());
    log.info("查询结果数量：" + pageList.getRecords().size());
    log.info("数据总数：" + pageList.getTotal());
    result.setSuccess(true);
    result.setResult(pageList);
    return result;
  }

  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public Result<SysAnnouncement> add(@RequestBody SysAnnouncement sysAnnouncement) {
    Result<SysAnnouncement> result = new Result<SysAnnouncement>();
    try {
      sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0);
      sysAnnouncement.setSendStatus(CommonSendStatus.UNPUBLISHED_STATUS_0);//未发布
      sysAnnouncementService.save(sysAnnouncement);
      result.success("添加成功！");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.error500("操作失败");
    }
    return result;
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.PUT)
  public Result<SysAnnouncement> eidt(@RequestBody SysAnnouncement sysAnnouncement) {
    Result<SysAnnouncement> result = new Result<>();
    SysAnnouncement sysAnnouncementEntity = sysAnnouncementService.getById(sysAnnouncement.getId());
    if (sysAnnouncementEntity == null) {
      result.error500("未找到对应实体");
    } else {
      boolean ok = sysAnnouncementService.saveOrUpdate(sysAnnouncement);
      //TODO 返回false说明什么？
      if (ok) {
        result.success("修改成功!");
      }
    }

    return result;
  }

  /**
   * 通过id删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
  public Result<SysAnnouncement> delete(@RequestParam(name = "id", required = true) String id) {
    Result<SysAnnouncement> result = new Result<SysAnnouncement>();
    SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(id);
    if (sysAnnouncement == null) {
      result.error500("未找到对应实体");
    } else {
      sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_1);
      boolean ok = sysAnnouncementService.updateById(sysAnnouncement);
      if (ok) {
        result.success("删除成功!");
      }
    }

    return result;
  }

  /**
   * 批量删除
   */
  @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
  public Result<SysAnnouncement> deleteBatch(
      @RequestParam(name = "ids", required = true) String ids) {
    Result<SysAnnouncement> result = new Result<SysAnnouncement>();
    if (ids == null || "".equals(ids.trim())) {
      result.error500("参数不识别！");
    } else {
      String[] id = ids.split(",");
      for (int i = 0; i < id.length; i++) {
        SysAnnouncement announcement = sysAnnouncementService.getById(id[i]);
        announcement.setDelFlag(CommonConstant.DEL_FLAG_1);
        sysAnnouncementService.updateById(announcement);
      }
      result.success("删除成功!");
    }
    return result;
  }

  /**
   * 通过id查询
   */
  @RequestMapping(value = "/queryById", method = RequestMethod.GET)
  public Result<SysAnnouncement> queryById(@RequestParam(name = "id", required = true) String id) {
    Result<SysAnnouncement> result = new Result<SysAnnouncement>();
    SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(id);
    if (sysAnnouncement == null) {
      result.error500("未找到对应实体");
    } else {
      result.setResult(sysAnnouncement);
      result.setSuccess(true);
    }
    return result;
  }

  /**
   * 更新发布操作
   */
  @RequestMapping(value = "/doReleaseData", method = RequestMethod.GET)
  public Result<SysAnnouncement> doReleaseData(
      @RequestParam(name = "id", required = true) String id, HttpServletRequest request) {
    Result<SysAnnouncement> result = new Result<SysAnnouncement>();
    SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(id);
    if (sysAnnouncement == null) {
      result.error500("未找到对应实体");
    } else {
      sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);//发布中
      sysAnnouncement.setSendTime(LocalDateTime.now());
      String currentUserName = AccountContextUtil.getAccountContext().getUserName();
      sysAnnouncement.setSender(currentUserName);
      boolean ok = sysAnnouncementService.updateById(sysAnnouncement);
      if (ok) {
        result.success("该系统通知发布成功");
        if (sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {
          JSONObject obj = new JSONObject();
          obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_TOPIC);
          obj.put(WebsocketConst.MSG_ID, sysAnnouncement.getId());
          obj.put(WebsocketConst.MSG_TXT, sysAnnouncement.getTitle());
          webSocket.sendAllMessage(obj.toJSONString(1));
        } else {
          // 2.插入用户通告阅读标记表记录
          String userId = sysAnnouncement.getUserIds();
          String[] userIds = userId.substring(0, (userId.length() - 1)).split(",");
          Long anntId = sysAnnouncement.getId();
          Date refDate = new Date();
          JSONObject obj = new JSONObject();
          obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
          obj.put(WebsocketConst.MSG_ID, sysAnnouncement.getId());
          obj.put(WebsocketConst.MSG_TXT, sysAnnouncement.getTitle());
          webSocket.sendMoreMessage(userIds, obj.toJSONString(1));
        }
      }
    }

    return result;
  }

  /**
   * 更新撤销操作
   */
  @RequestMapping(value = "/doReovkeData", method = RequestMethod.GET)
  public Result<SysAnnouncement> doReovkeData(@RequestParam(name = "id", required = true) String id,
      HttpServletRequest request) {
    Result<SysAnnouncement> result = new Result<SysAnnouncement>();
    SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(id);
    if (sysAnnouncement == null) {
      result.error500("未找到对应实体");
    } else {
      sysAnnouncement.setSendStatus(CommonSendStatus.REVOKE_STATUS_2);//撤销发布
      sysAnnouncement.setCancelTime(LocalDateTime.now());
      boolean ok = sysAnnouncementService.updateById(sysAnnouncement);
      if (ok) {
        result.success("该系统通知撤销成功");
      }
    }

    return result;
  }

  /**
   * @功能：补充用户数据，并返回系统消息
   */
  @RequestMapping(value = "/listByUser", method = RequestMethod.GET)
  public Result<Map<String, Object>> listByUser() {
    Result<Map<String, Object>> result = new Result<Map<String, Object>>();
    SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
    Long userId = sysUser.getId();
    // 1.将系统消息补充到用户通告阅读标记表中
    Collection<String> anntIds = sysAnnouncementSendService.queryByUserId(userId);
    LambdaQueryWrapper<SysAnnouncement> querySaWrapper = new LambdaQueryWrapper<SysAnnouncement>();
    querySaWrapper.eq(SysAnnouncement::getMsgType, CommonConstant.MSG_TYPE_ALL); // 全部人员
    querySaWrapper.eq(SysAnnouncement::getDelFlag, CommonConstant.DEL_FLAG_0.toString());  // 未删除
    querySaWrapper.eq(SysAnnouncement::getSendStatus, CommonConstant.HAS_SEND); //已发布
    querySaWrapper.ge(SysAnnouncement::getEndTime, sysUser.getCreateTime()); //新注册用户不看结束通知
    if (anntIds != null && anntIds.size() > 0) {
      querySaWrapper.notIn(SysAnnouncement::getId, anntIds);
    }
    List<SysAnnouncement> announcements = sysAnnouncementService.list(querySaWrapper);
    if (announcements.size() > 0) {
      for (int i = 0; i < announcements.size(); i++) {
        SysAnnouncementSend announcementSend = new SysAnnouncementSend();
        announcementSend.setAnntId(announcements.get(i).getId());
        announcementSend.setUserId(userId);
        announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
        sysAnnouncementSendService.save(announcementSend);
      }
    }
    // 2.查询用户未读的系统消息
    Page<SysAnnouncement> anntMsgList = new Page<SysAnnouncement>(0, 5);
    anntMsgList = sysAnnouncementService
        .querySysCementPageByUserId(anntMsgList, userId, "1");//通知公告消息
    Page<SysAnnouncement> sysMsgList = new Page<SysAnnouncement>(0, 5);
    sysMsgList = sysAnnouncementService.querySysCementPageByUserId(sysMsgList, userId, "2");//系统消息
    Map<String, Object> sysMsgMap = new HashMap<String, Object>();
    sysMsgMap.put("sysMsgList", sysMsgList.getRecords());
    sysMsgMap.put("sysMsgTotal", sysMsgList.getTotal());
    sysMsgMap.put("anntMsgList", anntMsgList.getRecords());
    sysMsgMap.put("anntMsgTotal", anntMsgList.getTotal());
    result.setSuccess(true);
    result.setResult(sysMsgMap);
    return result;
  }



  /**
   * 同步消息
   */
  @RequestMapping(value = "/syncNotic", method = RequestMethod.GET)
  public Result<SysAnnouncement> syncNotic(
      @RequestParam(name = "anntId", required = false) String anntId, HttpServletRequest request) {
    Result<SysAnnouncement> result = new Result<SysAnnouncement>();
    JSONObject obj = new JSONObject();
    if (StringUtils.isNotBlank(anntId)) {
      SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(anntId);
      if (sysAnnouncement == null) {
        result.error500("未找到对应实体");
      } else {
        if (sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {
          obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_TOPIC);
          obj.put(WebsocketConst.MSG_ID, sysAnnouncement.getId());
          obj.put(WebsocketConst.MSG_TXT, sysAnnouncement.getTitle());
          webSocket.sendAllMessage(obj.toJSONString(1));
        } else {
          // 2.插入用户通告阅读标记表记录
          String userId = sysAnnouncement.getUserIds();
          if (StringUtils.isNotBlank(userId)) {
            String[] userIds = userId.substring(0, (userId.length() - 1)).split(",");
            obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
            obj.put(WebsocketConst.MSG_ID, sysAnnouncement.getId());
            obj.put(WebsocketConst.MSG_TXT, sysAnnouncement.getTitle());
            webSocket.sendMoreMessage(userIds, obj.toJSONString(1));
          }
        }
      }
    } else {
      obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_TOPIC);
      obj.put(WebsocketConst.MSG_TXT, "批量设置已读");
      webSocket.sendAllMessage(obj.toJSONString(1));
    }
    return result;
  }
}
