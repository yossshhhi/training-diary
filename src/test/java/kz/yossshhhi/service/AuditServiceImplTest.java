package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.AuditRepository;
import kz.yossshhhi.dao.repository.UserRepository;
import kz.yossshhhi.dto.AuditDTO;
import kz.yossshhhi.mapper.AuditMapper;
import kz.yossshhhi.model.Audit;
import kz.yossshhhi.starter.audit.aop.model.AuditAction;
import kz.yossshhhi.starter.audit.aop.model.AuditType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Audit Service Tests")
class AuditServiceImplTest {

    @Mock
    private AuditRepository auditRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuditMapper auditMapper;
    @InjectMocks
    private AuditServiceImpl auditServiceImpl;

    @Test
    @DisplayName("Save method should save audit entry")
    void save_ShouldSaveAuditEntry() {
        Audit audit = Audit.builder().build();

        auditServiceImpl.save(audit);

        verify(auditRepository, times(1)).save(audit);
    }

    @Test
    @DisplayName("ShowAll method should return string representation of all audits")
    void showAll_ShouldReturnStringRepresentationOfAllAudits() {
        List<Audit> auditList = new ArrayList<>();
        auditList.add(Audit.builder().userId(1L).createdAt(LocalDateTime.now()).auditAction(AuditAction.REGISTRATION).auditType(AuditType.SUCCESS).build());
        auditList.add(Audit.builder().userId(2L).createdAt(LocalDateTime.now()).auditAction(AuditAction.LOG_IN).auditType(AuditType.FAIL).build());

        when(auditRepository.findAll()).thenReturn(auditList);

        List<AuditDTO> all = auditServiceImpl.findAll();
        assertEquals(auditList.size(), all.size());
    }
}
