package org.fxi.quick.securty.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.fxi.quick.common.vo.AccountContext;
import org.fxi.quick.securty.jwt.JwtToken;
import org.fxi.quick.securty.jwt.JwtUtil;
import org.fxi.quick.sys.entity.SysUser;

/**
 * 密码匹配器， 说白了就是验证密码用的
 */
public class JWTCredentialsMatcher implements CredentialsMatcher {


  @Override
  public boolean doCredentialsMatch(AuthenticationToken authenticationToken,
      AuthenticationInfo authenticationInfo) {
//    JwtToken jwtToken = (JwtToken) authenticationToken;
//    SysUser user = (SysUser) authenticationInfo.getPrincipals().getPrimaryPrincipal();
    // filter 已经检查
    return true;
  }
}
