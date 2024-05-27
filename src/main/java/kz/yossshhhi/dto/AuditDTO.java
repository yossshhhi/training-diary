package kz.yossshhhi.dto;


import kz.yossshhhi.starter.audit.aop.model.AuditAction;
import kz.yossshhhi.starter.audit.aop.model.AuditType;

import java.time.LocalDateTime;

public record AuditDTO(
        Long id,
        LocalDateTime createdAt,
        String username,
        AuditAction auditAction,
        AuditType auditType) {
}
