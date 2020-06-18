package org.fxi.quick.module.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.fxi.quick.common.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 系统通告表
 * @Author: jeecg-boot
 * @Date: 2019-01-02
 * @Version: V1.0
 */
@Data
@TableName("sys_announcement")
public class SysAnnouncement extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 标题
   */
  private String title;
  /**
   * 内容
   */
  private String msgContent;
  /**
   * 开始时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime startTime;
  /**
   * 结束时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime endTime;
  /**
   * 发布人
   */
  private String sender;
  /**
   * 优先级（L低，M中，H高）
   */
  private String priority;

  /**
   * 消息类型1:通知公告2:系统消息
   */
  private String msgCategory;
  /**
   * 通告对象类型（USER:指定用户，ALL:全体用户）
   */
  private String msgType;
  /**
   * 发布状态（0未发布，1已发布，2已撤销）
   */
  private Short sendStatus;
  /**
   * 发布时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime sendTime;
  /**
   * 撤销时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime cancelTime;

  /**
   * 创建人
   */
  private String createBy;

  /**
   * 更新人
   */
  private String updateBy;

  /**
   * 指定用户
   **/
  private String userIds;
  /**
   * 业务类型(email:邮件 bpm:流程)
   */
  private String busType;
  /**
   * 业务id
   */
  private String busId;
  /**
   * 打开方式 组件：component 路由：url
   */
  private String openType;
  /**
   * 组件/路由 地址
   */
  private String openPage;
  /**
   * 摘要
   */
  private String msgAbstract;
}
