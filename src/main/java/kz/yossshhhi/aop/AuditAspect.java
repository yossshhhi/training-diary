package kz.yossshhhi.aop;

import kz.yossshhhi.model.User;
import kz.yossshhhi.model.enums.AuditType;
import kz.yossshhhi.service.AuditService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AuditAspect {

    private AuditService auditService;

    public void initServices(AuditService auditService) {
        this.auditService = auditService;
    }

    @Pointcut("@annotation(auditable)")
    public void auditedMethods(Auditable auditable) {}


    @AfterReturning(value = "auditedMethods(auditable)", returning = "retVal", argNames = "retVal,auditable")
    public void afterSuccessfulAudit(Object retVal, Auditable auditable) {
        User user = (User) retVal;
        auditService.audit(user.getId(), auditable.action(), AuditType.SUCCESS);
    }

    @AfterThrowing(pointcut = "@annotation(auditable) && args(userId,..)", argNames = "auditable,userId")
    public void afterFailAudit(Auditable auditable, Long userId) {
        auditService.audit(userId, auditable.action(), AuditType.FAIL);
    }
}

