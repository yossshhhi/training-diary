package kz.yossshhhi.dto;

import kz.yossshhhi.model.enums.AuditAction;
import kz.yossshhhi.model.enums.AuditType;

import java.time.LocalDateTime;

public record AuditDTO(
        Long id,
        LocalDateTime createdAt,
        String username,
        AuditAction auditAction,
        AuditType auditType) {
}
