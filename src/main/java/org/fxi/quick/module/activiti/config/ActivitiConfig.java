package org.fxi.quick.module.activiti.config;

import java.io.IOException;
import javax.sql.DataSource;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 */
@Configuration//声名为配置类，继承Activiti抽象配置类
public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {

  @Bean
  public SpringProcessEngineConfiguration springProcessEngineConfiguration(
      PlatformTransactionManager transactionManager,
      SpringAsyncExecutor springAsyncExecutor,DataSource dataSource) throws IOException {
    SpringProcessEngineConfiguration springProcessEngineConfiguration = baseSpringProcessEngineConfiguration(
        dataSource,
        transactionManager,
        springAsyncExecutor);
    springProcessEngineConfiguration.setActivityFontName("宋体");
    springProcessEngineConfiguration.setLabelFontName("宋体");
    return springProcessEngineConfiguration;
  }
}
