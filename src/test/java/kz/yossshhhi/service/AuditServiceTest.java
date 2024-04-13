package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.AuditRepository;
import kz.yossshhhi.model.Audit;
import kz.yossshhhi.model.enums.AuditAction;
import kz.yossshhhi.model.enums.AuditType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Audit Service Tests")
class AuditServiceTest {

    @Mock
    private AuditRepository auditRepository;

    private AuditService auditService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        auditService = new AuditService(auditRepository);
    }

    @Test
    @DisplayName("Save method should save audit entry")
    void save_ShouldSaveAuditEntry() {
        Audit audit = Audit.builder().build();

        auditService.save(audit);

        verify(auditRepository, times(1)).save(audit);
    }

    @Test
    @DisplayName("ShowAll method should return string representation of all audits")
    void showAll_ShouldReturnStringRepresentationOfAllAudits() {
        List<Audit> auditList = new ArrayList<>();
        auditList.add(Audit.builder().userId(1L).createdAt(LocalDateTime.now()).auditAction(AuditAction.REGISTRATION).auditType(AuditType.SUCCESS).build());
        auditList.add(Audit.builder().userId(2L).createdAt(LocalDateTime.now()).auditAction(AuditAction.LOG_IN).auditType(AuditType.FAIL).build());

        when(auditRepository.findAll()).thenReturn(auditList);

        String auditString = auditService.showAll();

        String expectedString = auditList.get(0).toString() + "\n" + auditList.get(1).toString() + "\n";
        assertEquals(expectedString, auditString);
    }
}
