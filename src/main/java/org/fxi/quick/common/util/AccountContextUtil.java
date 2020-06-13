package org.fxi.quick.common.util;

import javax.servlet.http.HttpServletRequest;
import org.fxi.quick.common.vo.AccountContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 用户上下文工具类
 * @author initializer
 * @date 2018-11-30 8:11
 */
public class AccountContextUtil {

    private static final String ATTRIBUTE_ACCOUNT_CONTEXT = "__ACCOUNT_CONTEXT__";

    /**
     * 获取当前登陆账户上下文
     *
     * @return
     */
    public static AccountContext getAccountContext() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            throw new IllegalStateException("当前线程中不存在 Request 上下文");
        }
        return (AccountContext) request.getAttribute(ATTRIBUTE_ACCOUNT_CONTEXT);
    }

    /**
     * 获取当前线程请求对象
     * @return
     * @throws IllegalStateException
     */
    static HttpServletRequest getCurrentRequest() throws IllegalStateException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前线程中不存在 Request 上下文");
        }
        return attrs.getRequest();
    }

    /**
     * 设置登陆账户上下文到当前请求中
     * @param request
     * @param accountContext
     */
    public static void setAccountContext(HttpServletRequest request, AccountContext accountContext) {
        request.setAttribute(ATTRIBUTE_ACCOUNT_CONTEXT, accountContext);
    }
}
