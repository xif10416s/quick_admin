package org.fxi.quick.securty.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Date;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.fxi.quick.common.exception.BizException;
import org.fxi.quick.common.util.AccountContextUtil;
import org.fxi.quick.common.vo.AccountContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

  private JwtConfig jwtConfig;

  public JwtFilter(JwtConfig jwtConfig){
    this.jwtConfig = jwtConfig;
  }


  /**
   * 执行登录认证
   *
   * @param request
   * @param response
   * @param mappedValue
   * @return
   */
  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    try {
      executeLogin(request, response);
      return true;
    } catch (BizException e) {
      response401(request,response,e.getMessage());
    } catch (Exception e) {
      response401(request,response,e.getMessage());
    }
    return false;
  }

  /**
   * 执行登录操作
   */
  @Override
  protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    String token = httpServletRequest.getHeader("Authorization");
    if (StringUtils.isBlank(token)) {
      log.error("获取不到Token");
      throw new BizException("A0301");
    }

    try {
      Claims claims = JwtUtil.parseToken(StringUtils.substringAfter(token, "Bearer "),
          jwtConfig.getSecret());
      if (claims == null) {
        log.error("解析Token失败");
        throw new BizException("A0301");
      }
      Long userId = Long.valueOf(claims.getSubject());
      AccountContext accountCtx = new AccountContext(userId,(String) claims.get("userName"),(Integer) claims.get("userType"));
      log.trace("设置用户信息到当前请求, userId={}", userId);
      AccountContextUtil.setAccountContext(httpServletRequest, accountCtx);

      // 时间到了最长过期时间的一半时，即生成新的token TODO
      int mv2 = -2;
      if (DateUtils.addSeconds(claims.getExpiration(),
          (int) (jwtConfig.getExpiredTime() / mv2)).getTime() < (new Date())
          .getTime()) {
        httpServletResponse.setHeader("Authorization", JwtUtil.createToken(accountCtx));
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Authorization");
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
      }

    } catch (ExpiredJwtException be) {
      log.error("Token 过期：{}", httpServletRequest.getRequestURI(), be);
      throw new BizException("A0311");
    } catch (Exception e) {
      log.error("验证 Token 时出现异常 {}", httpServletRequest.getRequestURI(), e);
      throw new BizException("A0300");
    }

    JwtToken jwtToken = new JwtToken(token);
    // 提交给realm进行登入，如果错误他会抛出异常并被捕获
    getSubject(request, response).login(jwtToken);
    // 如果没有抛出异常则代表登入成功，返回true
    return true;
  }

  /**
   * 对跨域提供支持
   */
  @Override
  protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
    httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
    httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
    httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
    httpServletResponse.setHeader("Content-Type","application/json;charset=UTF-8");
    // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
    if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
      httpServletResponse.setStatus(HttpStatus.OK.value());
      return false;
    }
    return super.preHandle(request, response);
  }

  /**
   * 将非法请求跳转到 /401
   */
  private void response401(ServletRequest req, ServletResponse resp , String message) {
    try {
      HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
      HttpServletRequest httpServletRequest = (HttpServletRequest)req;
      httpServletRequest.getSession().setAttribute("msg",message);
      httpServletResponse.sendRedirect(req.getServletContext().getContextPath() + "/sys/common/401");
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
