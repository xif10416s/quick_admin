package org.fxi.quick;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author initializer
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"org.fxi.quick"} , exclude = DruidDataSourceAutoConfigure.class)
@EnableAspectJAutoProxy
@EnableAsync
public class BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);

        // kill -15 (ctrl + c) 后续操作
        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
                //TODO 执行完任务
                System.out.println("BaseApplication shutdown...");
        }));
    }

    /**
     * 接收到 shutdown 信号后，继续完成当前请求，并停止接收新请求
     *
     * @return
     */
    @Bean
    public ServletWebServerFactory servletContainer(GracefulShutdownTomcat gracefulShutdownTomcat) {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers(gracefulShutdownTomcat);
        return tomcat;
    }

    @Bean
    public GracefulShutdownTomcat gracefulShutdownTomcat() {
        return new GracefulShutdownTomcat();
    }

    private static class GracefulShutdownTomcat implements TomcatConnectorCustomizer,
        ApplicationListener<ContextClosedEvent> {

        private static final Logger log = LoggerFactory.getLogger(GracefulShutdownTomcat.class);

        private volatile Connector connector;

        @Override
        public void customize(Connector connector) {
            this.connector = connector;
        }

        long timeWait = 30;

        @Override
        public void onApplicationEvent(ContextClosedEvent event) {
            this.connector.pause();
            Executor executor = this.connector.getProtocolHandler().getExecutor();
            if (executor instanceof ThreadPoolExecutor) {
                try {
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                    threadPoolExecutor.shutdown();
                    if (!threadPoolExecutor.awaitTermination(timeWait, TimeUnit.SECONDS)) {
                        log.warn(
                            "Tomcat thread pool did not shut down gracefully within " + timeWait
                                + " seconds. Proceeding with forceful shutdown");
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }
}
