package org.fxi.quick.common.i18n;

import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author initializer
 * @date 2018-11-30 17:47
 */
@Component
@Slf4j
public class MessageHelper {

    private static MessageSource messageSource;

    public static String getMessage(String code, Object[] params) {
        String message = "";
        try {
            Locale locale = LocaleContextHolder.getLocale();
            message = messageSource.getMessage(code, params, locale);
        } catch (Exception e) {
            log.error("parse message error! ", e);
        }
        return message;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        MessageHelper.messageSource = messageSource;
    }
}
