package kz.yossshhhi.dao.repository;

import kz.yossshhhi.model.Audit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Audit Repository Tests")
@ExtendWith(MockitoExtension.class)
public class AuditRepositoryTest {

    @Mock
    private AuditRepository auditRepository;

    @Test
    @DisplayName("Saving and retrieving audit should return the same audit")
    void saveAndFindAll_ShouldReturnSameAudit() {
        Audit auditToSave = mock(Audit.class);
        Audit savedAudit = mock(Audit.class);
        when(auditRepository.save(any(Audit.class))).thenReturn(savedAudit);
        when(auditRepository.findAll()).thenReturn(Collections.singletonList(savedAudit));

        Audit returnedAudit = auditRepository.save(auditToSave);
        List<Audit> retrievedAudits = auditRepository.findAll();

        assertThat(returnedAudit)
                .as("Check that the saved audit has the correct properties")
                .isNotNull()
                .isEqualToComparingFieldByField(savedAudit);

        assertThat(retrievedAudits)
                .as("Ensure the retrieved list contains the saved audit")
                .isNotNull()
                .hasSize(1)
                .containsExactly(savedAudit);

        verify(auditRepository).save(auditToSave);
        verify(auditRepository).findAll();
    }
}