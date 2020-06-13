package org.fxi.quick.securty.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.fxi.quick.common.exception.BizException;
import org.fxi.quick.common.vo.AccountContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author initializer
 * @date 2018-11-30 9:41
 */
@Component
public class JwtUtil {

    private static JwtConfig jwtConfig;

    private static final String TOKEN_PREFIX = "RP-";

    /**
     * 创建Token
     * @param accountContext 用户上下文
     * @return
     */
    public static String createToken(AccountContext accountContext) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("userName", accountContext.getUserName());
        claims.put("userType", accountContext.getUserType());
        return TOKEN_PREFIX + createToken(claims, String.valueOf(accountContext.getUserId()));
    }

    /**
     * 创建Token
     * @param claims 私有载荷
     * @param subject 主题
     * @return jws
     */
    public static String createToken(Map<String, Object> claims, String subject) {
        Key key = generateKey(jwtConfig.getSecret());
        long now = System.currentTimeMillis();
        long expiredAt = now + jwtConfig.getExpiredTime() * 1000;

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .signWith(key)
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(expiredAt))
            .compact();
    }

    /**
     * 获取Token
     * @param token
     * @param secret
     * @return
     * @throws Exception
     */
    public static Claims parseToken(String token, String secret) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        token = token.substring(TOKEN_PREFIX.length());
        try {
            return Jwts.parser()
                .setSigningKey(generateKey(secret))
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw e;
        }
    }

    /**
     * 生成签名Key
     * @param secret 签名密钥
     * @return
     */
    static SecretKey generateKey(String secret) {
        try {
            return Keys.hmacShaKeyFor(secret.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Autowired
    public void setJwtConfig(JwtConfig jwtConfig) {
        JwtUtil.jwtConfig = jwtConfig;
    }
}
