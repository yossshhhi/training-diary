package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.AuditRepository;
import kz.yossshhhi.model.Audit;
import kz.yossshhhi.model.enums.AuditAction;
import kz.yossshhhi.model.enums.AuditType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for auditing user actions.
 */
public class AuditService {
    /**
     * The repository for accessing audit data.
     */
    private final AuditRepository auditRepository;

    /**
     * Constructs an instance of AuditService.
     *
     * @param auditRepository The repository for accessing audit data.
     */
    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    /**
     * Records an audit entry for a user action.
     *
     * @param userId The ID of the user performing the action.
     * @param action The action performed by the user.
     * @param type   The type of action being audited.
     */
    public void audit(Long userId, AuditAction action, AuditType type) {
        Audit audit = Audit.builder()
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .auditAction(action)
                .auditType(type)
                .build();
        save(audit);
    }

    /**
     * Saves an audit entry.
     *
     * @param audit The audit entry to save.
     * @return The saved audit entry.
     */
    public Audit save(Audit audit) {
        return auditRepository.save(audit);
    }

    /**
     * Retrieves all audit entries and formats them as a string.
     *
     * @return A string representation of all audit entries.
     */
    public List<Audit> findAll() {
        return auditRepository.findAll();
    }
}

