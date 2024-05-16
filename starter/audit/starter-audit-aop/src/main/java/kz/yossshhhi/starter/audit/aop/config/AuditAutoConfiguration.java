package kz.yossshhhi.starter.audit.aop.config;

import kz.yossshhhi.starter.audit.aop.aspect.AuditAspect;
import kz.yossshhhi.starter.audit.aop.service.AuditService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AuditAutoConfiguration {

    @Bean
    @ConditionalOnBean(AuditService.class)
    public AuditAspect auditAspect(AuditService auditService) {
        return new AuditAspect(auditService);
    }
}
