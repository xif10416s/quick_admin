package org.fxi.quick.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.fxi.quick.common.i18n.MessageHelper;

/**
 * @author initializer
 * @date 2018-11-30 17:31
 */
@NoArgsConstructor
@Getter
public class BizException extends RuntimeException {

    private String code;

    private String message;

    public BizException(String errorCode, Object... params) {
        this.code = errorCode;
        this.message = MessageHelper.getMessage(errorCode, params);
    }
}
