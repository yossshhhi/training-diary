package kz.yossshhhi.model;

import kz.yossshhhi.model.enums.AuditAction;
import kz.yossshhhi.model.enums.AuditType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@ToString
public class Audit {
    /**
     * The unique identifier for the audit.
     */
    Long id;

    /**
     * The timestamp when the audit was created.
     */
    LocalDateTime createdAt;

    /**
     * The identifier of the user associated with the audit.
     */
    Long userId;

    /**
     * The action performed during the audit.
     */
    AuditAction auditAction;

    /**
     * The type of audit.
     */
    AuditType auditType;
}
