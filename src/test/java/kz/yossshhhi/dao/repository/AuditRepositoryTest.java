package kz.yossshhhi.dao.repository;

import kz.yossshhhi.container.TestContainerInitializer;
import kz.yossshhhi.dao.AuditDAO;
import kz.yossshhhi.model.Audit;
import kz.yossshhhi.model.enums.AuditAction;
import kz.yossshhhi.model.enums.AuditType;
import kz.yossshhhi.util.ResultSetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
public class AuditRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static AuditRepository auditRepository;

    @BeforeEach
    void setUp() {
        TestContainerInitializer.initializeDatabase(postgreSQLContainer);
        auditRepository = new AuditDAO(TestContainerInitializer.databaseManager(postgreSQLContainer), new ResultSetMapper<>(Audit.class));
    }

    @Test
    @DisplayName("Saving and retrieving audit should return the same audit")
    void saveAndFindAll_ShouldReturnSameAudit() {
        Audit audit = Audit.builder()
                .createdAt(LocalDateTime.now())
                .userId(1L)
                .auditAction(AuditAction.REGISTRATION)
                .auditType(AuditType.SUCCESS)
                .build();

        Audit savedAudit = auditRepository.save(audit);

        List<Audit> allAudits = auditRepository.findAll();

        assertNotNull(savedAudit.getId());
        assertEquals(allAudits.get(0).getId(), savedAudit.getId());
        assertEquals(allAudits.get(0).getUserId(), savedAudit.getUserId());
        assertEquals(allAudits.get(0).getAuditAction(), savedAudit.getAuditAction());
        assertEquals(allAudits.get(0).getAuditType(), savedAudit.getAuditType());
        assertEquals(allAudits.size(), 1);
    }
}