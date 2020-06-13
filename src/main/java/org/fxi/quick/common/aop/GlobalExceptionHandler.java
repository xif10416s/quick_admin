package org.fxi.quick.common.aop;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.fxi.quick.common.api.vo.Result;
import org.fxi.quick.common.constant.CommonConstant;
import org.fxi.quick.common.exception.BizException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author initializer
 * @date 2018-12-01 17:34
 * 不能处理Filter的异常，包括认证异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     *
     * @param bizException
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    public Result<?> handleBizException(BizException bizException) {
        log.error(ExceptionUtils.getStackTrace(bizException));
        return Result.error(bizException.getMessage());
    }

    /**
     * 数据绑定异常处理
     *
     * @param e
     * @return
     */
//    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
//    public RestResponse handleBindException(Exception e) {
//        BindingResult br = null;
//        if (e instanceof BindException) {
//            br = ((BindException) e).getBindingResult();
//        } else if (e instanceof MethodArgumentNotValidException) {
//            br = ((MethodArgumentNotValidException) e).getBindingResult();
//        }
//        Validate.notNull(br, "Should not execute here!");
//
//        StringBuilder sb = new StringBuilder("Parameter Error:");
//        for (ObjectError objErr : br.getAllErrors()) {
//            sb.append(" ");
//            if (objErr instanceof FieldError) {
//                FieldError error = (FieldError) objErr;
//                sb.append(error.getObjectName()).append(".").append(error.getField()).append(" ").append(error.getDefaultMessage());
//            } else {
//                sb.append(objErr.toString());
//            }
//            sb.append(";");
//        }
//
//        return RestResponse.fail(400, sb.toString().trim());
//    }
//
//    /**
//     * RequestParam异常
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
//    public RestResponse handleRequestParamException(Exception e) {
//        return RestResponse.fail(400, e.getMessage());
//    }


    /**
     * 未捕获的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    public Result<?> handleUnauthorizedException(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return Result.error(CommonConstant.RESPONSE_CODE_403, e.getMessage());
    }


    /**
     * 未捕获的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> handleException(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, "服务器开小差了...");
    }
}
