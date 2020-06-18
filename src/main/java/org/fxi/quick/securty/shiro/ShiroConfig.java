package org.fxi.quick.securty.shiro;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.fxi.quick.securty.jwt.JwtConfig;
import org.fxi.quick.securty.jwt.JwtFilter;
import org.fxi.quick.module.sys.service.ISysUserService;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.DelegatingFilterProxy;

@Slf4j
@Configuration
@Component
public class ShiroConfig {


  /**
   * JWT过滤器名称
   */
  private static final String JWT_FILTER_NAME = "jwtFilter";
  /**
   * 请求路径过滤器名称
   */
  private static final String REQUEST_PATH_FILTER_NAME = "path";
  /**
   * Shiro过滤器名称
   */
  private static final String SHIRO_FILTER_NAME = "shiroFilter";


  /**
   * 身份匹配验证
   * @return
   */
  @Bean
  public CredentialsMatcher credentialsMatcher() {
    return new JWTCredentialsMatcher();
  }


  /**
   * JWT数据源验证
   * JWTRealm既要验证身份，又要做权限认证
   * @return
   */
  @Bean
  public JwtRealm jwtRealm(ISysUserService userService,JwtConfig jwtConfig) {
    JwtRealm jwtRealm = new JwtRealm(userService,jwtConfig);
    jwtRealm.setCachingEnabled(false);
    jwtRealm.setCredentialsMatcher(credentialsMatcher());
    return jwtRealm;
  }


  /**
   *  关闭shiro自带的session
   * @return
   */
  @Bean
  public SessionStorageEvaluator sessionStorageEvaluator() {
    DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
    sessionStorageEvaluator.setSessionStorageEnabled(false);
    return sessionStorageEvaluator;
  }

  @Bean
  public DefaultSubjectDAO subjectDAO() {
    DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
    defaultSubjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
    return defaultSubjectDAO;
  }

  /**
   * 安全管理器配置
   *
   * @return
   */
  @Bean
  public DefaultWebSecurityManager securityManager( ISysUserService userService,JwtConfig jwtConfig) {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    securityManager.setRealm(jwtRealm(userService,jwtConfig));
    securityManager.setSubjectDAO(subjectDAO());
    SecurityUtils.setSecurityManager(securityManager);
    return securityManager;
  }

  /**
   * ShiroFilterFactoryBean配置
   *
   * @param securityManager
   * @return
   */
  @Bean(SHIRO_FILTER_NAME)
  public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,JwtConfig jwtConfig) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    shiroFilterFactoryBean.setSecurityManager(securityManager);
    Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
    filters.put(JWT_FILTER_NAME, new JwtFilter(jwtConfig));
    shiroFilterFactoryBean.setFilters(filters);

    // 未授权界面返回JSON
    shiroFilterFactoryBean.setUnauthorizedUrl("/sys/common/401");
    shiroFilterFactoryBean.setLoginUrl("/sys/common/401");
    shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
    return shiroFilterFactoryBean;
  }


  @Bean
  protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
    DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
    chainDefinition.addPathDefinition("/sys/login", "anon");  //login不做认证
    chainDefinition.addPathDefinition("/sys/logout", "anon"); //
    chainDefinition.addPathDefinition("/sys/common/401", "anon");


    //性能监控
    chainDefinition.addPathDefinition("/actuator/metrics/**", "anon");
    chainDefinition.addPathDefinition("/actuator/httptrace/**", "anon");
    chainDefinition.addPathDefinition("/actuator/redis/**", "anon");

    chainDefinition.addPathDefinition("/druid/**", "anon");
    chainDefinition.addPathDefinition("/swagger-ui.html", "anon");
    chainDefinition.addPathDefinition("/swagger**/**", "anon");
    chainDefinition.addPathDefinition("/webjars/**", "anon");
    chainDefinition.addPathDefinition("/v2/**", "anon");


    // <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
    chainDefinition.addPathDefinition("/**", JWT_FILTER_NAME);
    return chainDefinition;
  }



  /**
   * ShiroFilter配置
   *
   * @return
   */
  @Bean
  public FilterRegistrationBean delegatingFilterProxy() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    DelegatingFilterProxy proxy = new DelegatingFilterProxy();
    proxy.setTargetFilterLifecycle(true);
    proxy.setTargetBeanName(SHIRO_FILTER_NAME);
    filterRegistrationBean.setFilter(proxy);
    filterRegistrationBean.setAsyncSupported(true);
    filterRegistrationBean.setEnabled(true);
    filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
    return filterRegistrationBean;
  }

  @Bean
  public Authenticator authenticator(ISysUserService userService,JwtConfig jwtConfig) {
    ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
    authenticator.setRealms(Arrays.asList(jwtRealm(userService,jwtConfig)));
    authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
    return authenticator;
  }


  /**
   * Enabling Shiro Annotations
   *
   * @return
   */
  @Bean
  public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
    return authorizationAttributeSourceAdvisor;
  }

  /**
   * RequiresPermissions这行去掉，是可以正常访问的，加上之后就是404。
   * @return
   */
  @Bean
  public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
    DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
    defaultAdvisorAutoProxyCreator.setUsePrefix(true);
    defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
    return defaultAdvisorAutoProxyCreator;
  }


}
