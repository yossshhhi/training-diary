package kz.yossshhhi.model;

import kz.yossshhhi.starter.audit.aop.model.AuditAction;
import kz.yossshhhi.starter.audit.aop.model.AuditType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Audit {
    /**
     * The unique identifier for the audit.
     */
    private Long id;

    /**
     * The timestamp when the audit was created.
     */
    private LocalDateTime createdAt;

    /**
     * The identifier of the user associated with the audit.
     */
    private Long userId;
    private String username;

    /**
     * The action performed during the audit.
     */
    private AuditAction auditAction;

    /**
     * The type of audit.
     */
    private AuditType auditType;
}
