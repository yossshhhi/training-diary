package kz.yossshhhi.starter.audit.aop.service;

import kz.yossshhhi.starter.audit.aop.model.AuditAction;
import kz.yossshhhi.starter.audit.aop.model.AuditType;

public interface AuditService {
    void audit(Long userId, AuditAction action, AuditType type);
    void audit(String username, AuditAction action, AuditType type);
}
