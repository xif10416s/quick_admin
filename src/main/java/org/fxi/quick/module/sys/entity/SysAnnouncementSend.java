package org.fxi.quick.module.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.fxi.quick.common.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date: 2019-02-21
 * @Version: V1.0
 */
@Data
@TableName("sys_announcement_send")
public class SysAnnouncementSend extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;


  /**
   * 通告id
   */
  private Long anntId;
  /**
   * 用户id
   */
  private Long userId;
  /**
   * 阅读状态（0未读，1已读）
   */
  private Short readFlag;
  /**
   * 阅读时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime readTime;
  /**
   * 创建人
   */
  private String createBy;

  /**
   * 更新人
   */
  private String updateBy;

}
