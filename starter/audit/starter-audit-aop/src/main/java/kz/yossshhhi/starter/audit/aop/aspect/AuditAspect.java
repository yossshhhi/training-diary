package kz.yossshhhi.starter.audit.aop.aspect;

import kz.yossshhhi.starter.audit.aop.annotation.Auditable;
import kz.yossshhhi.starter.audit.aop.model.AuditType;
import kz.yossshhhi.starter.audit.aop.model.AuthenticatedRequest;
import kz.yossshhhi.starter.audit.aop.model.Identifiable;
import kz.yossshhhi.starter.audit.aop.service.AuditService;
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
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
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
     * @param retVal the return value of the audited method, expected to be of type {@link Identifiable}
     * @param auditable the annotation applied to the audited method, which contains metadata for the audit
     */
    @AfterReturning(value = "auditedMethods(auditable)", returning = "retVal", argNames = "retVal,auditable")
    public void afterSuccessfulAudit(Object retVal, Auditable auditable) {
        if (retVal instanceof Identifiable identifiable) {
            auditService.audit(identifiable.getId(), auditable.action(), AuditType.SUCCESS);
        }
    }

    /**
     * Advice that is executed when a method annotated with {@link Auditable} throws an exception.
     * It records a failed audit event using the username provided in the {@link AuthenticatedRequest}.
     *
     * @param auditable the annotation applied to the audited method, which contains metadata for the audit
     * @param request the {@link AuthenticatedRequest} argument of the audited method, used to extract the username
     */
    @AfterThrowing(pointcut = "execution(* *(..)) && @annotation(auditable) && args(request,..)", argNames = "auditable,request")
    public void afterFailAudit(Auditable auditable, Object request) {
        if (request instanceof AuthenticatedRequest authenticatedRequest) {
            auditService.audit(authenticatedRequest.getUsername(), auditable.action(), AuditType.FAIL);
        }
    }
}
