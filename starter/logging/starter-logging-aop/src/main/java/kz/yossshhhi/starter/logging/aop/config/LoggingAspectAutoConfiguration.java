package kz.yossshhhi.starter.logging.aop.config;

import kz.yossshhhi.starter.logging.aop.aspect.LoggableAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectAutoConfiguration {

    @Bean
    public LoggableAspect loggableAspect() {
        return new LoggableAspect();
    }
}
