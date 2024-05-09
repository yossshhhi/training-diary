package kz.yossshhhi.aop;

import kz.yossshhhi.dto.AuthenticationDTO;
import kz.yossshhhi.model.User;
import kz.yossshhhi.model.enums.AuditType;
import kz.yossshhhi.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Aspect for auditing activities within the application.
 * This aspect handles both successful executions and exceptions to methods annotated with {@link Auditable}.
 * It records audit events through the {@link AuditService}.
 */
@Aspect
@RequiredArgsConstructor
public class AuditAspect {

    private AuditService auditService;

    /**
     * Initializes the {@link AuditService} used for auditing actions.
     * This method is intended for setting the audit service dependency manually.
     *
     * @param auditService the audit service to use for recording audit events
     */
    public void initServices(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Pointcut that matches all method executions where the method is annotated with {@link Auditable}.
     *
     * @param auditable the annotation that triggers this pointcut
     */
    @Pointcut("execution(* *(..)) && @annotation(auditable)")
    public void auditedMethods(Auditable auditable) {}

    /**
     * Advice that is executed after a method annotated with {@link Auditable} successfully returns.
     * It records a successful audit event for the user returned by the method.
     *
     * @param retVal the return value of the audited method, expected to be of type {@link User}
     * @param auditable the annotation applied to the audited method, which contains metadata for the audit
     */
    @AfterReturning(value = "auditedMethods(auditable)", returning = "retVal", argNames = "retVal,auditable")
    public void afterSuccessfulAudit(Object retVal, Auditable auditable) {
        User user = (User) retVal;
        auditService.audit(user.getId(), auditable.action(), AuditType.SUCCESS);
    }

    /**
     * Advice that is executed when a method annotated with {@link Auditable} throws an exception.
     * It records a failed audit event using the username provided in the {@link AuthenticationDTO}.
     *
     * @param auditable the annotation applied to the audited method, which contains metadata for the audit
     * @param request the {@link AuthenticationDTO} argument of the audited method, used to extract the username
     */
    @AfterThrowing(pointcut = "execution(* *(..)) && @annotation(auditable) && args(request,..)", argNames = "auditable,request")
    public void afterFailAudit(Auditable auditable, AuthenticationDTO request) {
        auditService.audit(request.username(), auditable.action(), AuditType.FAIL);
    }
}

