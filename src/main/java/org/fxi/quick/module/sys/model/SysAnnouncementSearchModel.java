package org.fxi.quick.module.sys.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.fxi.quick.common.entity.BaseEntity;
import org.fxi.quick.common.model.PageSearchModel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: jeecg-boot
 * @Date: 2019-01-02
 * @Version: V1.0
 */
@Data
@TableName("sys_announcement")
public class SysAnnouncementSearchModel extends PageSearchModel implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 标题
   */
  private String title;
  /**
   * 内容
   */
  private String msgContent;
}
