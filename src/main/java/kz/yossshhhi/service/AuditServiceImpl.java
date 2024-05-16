package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.AuditRepository;
import kz.yossshhhi.dao.repository.UserRepository;
import kz.yossshhhi.dto.AuditDTO;
import kz.yossshhhi.mapper.AuditMapper;
import kz.yossshhhi.model.Audit;
import kz.yossshhhi.model.User;
import kz.yossshhhi.starter.audit.aop.model.AuditAction;
import kz.yossshhhi.starter.audit.aop.model.AuditType;
import kz.yossshhhi.starter.audit.aop.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for auditing user actions.
 */
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    /**
     * The repository for accessing audit data.
     */
    private final AuditRepository auditRepository;
    private final UserRepository userRepository;
    private final AuditMapper auditMapper;

    /**
     * Records an audit entry for a user action.
     *
     * @param userId The ID of the user performing the action.
     * @param action The action performed by the user.
     * @param type   The type of action being audited.
     */
    @Override
    public void audit(Long userId, AuditAction action, AuditType type) {
        Audit audit = Audit.builder()
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .auditAction(action)
                .auditType(type)
                .build();
        save(audit);
    }

    @Override
    public void audit(String username, AuditAction action, AuditType type) {
        Optional<User> optional = userRepository.findByUsername(username);
        User user = optional.orElseGet(() -> User.builder().id(1L).build());
        Audit audit = Audit.builder()
                .userId(user.getId())
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
    public List<AuditDTO> findAll() {
        List<Audit> all = auditRepository.findAll();
        return all.stream().map(auditMapper::toDTO).toList();
    }
}

