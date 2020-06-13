package org.fxi.quick.securty.jwt;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author initializer
 * @date 2018-11-29 16:40
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "jjwt")
@Data
public class JwtConfig {

    /**
     * 密钥
     */
    private String secret;

    /**
     * 过期时间，单位：秒
     */
    private Long expiredTime;

    /**
     * 可信任的请求来源IP地址
     */
    private List<String> trustIps;
}
