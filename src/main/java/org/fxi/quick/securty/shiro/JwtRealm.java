package org.fxi.quick.securty.shiro;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.fxi.quick.securty.jwt.JwtConfig;
import org.fxi.quick.securty.jwt.JwtToken;
import org.fxi.quick.securty.jwt.JwtUtil;
import org.fxi.quick.sys.entity.SysUser;
import org.fxi.quick.sys.service.ISysUserService;

@Slf4j
public class JwtRealm extends AuthorizingRealm {

  private JwtConfig jwtConfig;

  private ISysUserService userService;

  public JwtRealm(ISysUserService userService, JwtConfig jwtConfig) {
    this.userService = userService;
    this.jwtConfig = jwtConfig;
  }

  @Override
  public boolean supports(AuthenticationToken token) {
    return token != null && token instanceof JwtToken;
  }

  /**
   * 授权认证,设置角色/权限信息 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    log.debug("doGetAuthorizationInfo principalCollection...");
    // 设置角色/权限信息
    SysUser user = (SysUser) principalCollection.getPrimaryPrincipal();
    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    // 设置角色
    Set<String> userRolesSet = userService.getUserRolesSet(user.getId());
    authorizationInfo.setRoles(userRolesSet);
    // 设置权限
    Set<String> userPermissionsSet = userService.getUserPermissionsSet(user.getId());
    authorizationInfo.setStringPermissions(userPermissionsSet);
    return authorizationInfo;
  }

  /**
   * 登陆认证
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
      throws AuthenticationException {
    log.debug("doGetAuthenticationInfo authenticationToken...");
    // 校验token
    JwtToken jwtToken = (JwtToken) authenticationToken;
    if (jwtToken == null) {
      throw new AuthenticationException("jwtToken不能为空");
    }

    String token = jwtToken.getPrincipal().toString();
    SysUser sysUser = checkUserTokenIsEffect(token);

    return new SimpleAuthenticationInfo(
        sysUser,
        token,
        getName()
    );

  }

  /**
   * 校验token的有效性
   */
  public SysUser checkUserTokenIsEffect(String token) throws AuthenticationException {
    Long userId = null;
    try {
      Claims claims = JwtUtil
          .parseToken(StringUtils.substringAfter(token, "Bearer "),
              jwtConfig.getSecret());
      userId = Long.valueOf(claims.getSubject());
    } catch (
        ExpiredJwtException be) {
      log.error("Token 过期：{}", be);
      throw new AuthenticationException("Token 过期!");
    }

    if (userId == null) {
      throw new AuthenticationException("token非法无效!");
    }

    // 查询用户信息
    log.debug("———校验token是否有效————checkUserTokenIsEffect——————— " + token);
    SysUser loginUser = userService.getBaseMapper().selectById(userId);
    if (loginUser == null) {
      throw new AuthenticationException("用户不存在!");
    }
    // 判断用户状态
    if (loginUser.getStatus() != 1) {
      throw new AuthenticationException("账号已被锁定,请联系管理员!");
    }

    return loginUser;
  }

}
