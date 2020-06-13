package org.fxi.quick.common.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户上下文
 *
 * @author initializer
 * @date 2018-11-30 8:14
 */
@Data
public class AccountContext implements Serializable {

    private static final long serialVersionUID = -8372457239208831301L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户类型
     */
    private Integer userType;

    public AccountContext(Long userId, String userName , Integer userType){
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
    }
}
