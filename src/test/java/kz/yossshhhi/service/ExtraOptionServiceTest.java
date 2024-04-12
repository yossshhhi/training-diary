package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionRepository;
import kz.yossshhhi.model.ExtraOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExtraOptionServiceTest {

    @Mock
    private ExtraOptionRepository extraOptionRepository;

    private ExtraOptionService extraOptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        extraOptionService = new ExtraOptionService(extraOptionRepository);
    }

    @Test
    void create_ExtraOptionDoesNotExist_ShouldCreateExtraOption() {
        ExtraOption extraOption = ExtraOption.builder().build();
        extraOption.setName("Option");

        when(extraOptionRepository.findByName(extraOption.getName())).thenReturn(Optional.empty());
        when(extraOptionRepository.save(extraOption)).thenReturn(extraOption);

        ExtraOption createdExtraOption = extraOptionService.create(extraOption);

        assertNotNull(createdExtraOption);
        verify(extraOptionRepository, times(1)).save(extraOption);
        assertEquals(extraOption, createdExtraOption);
    }

    @Test
    void findById_ExistingId_ShouldReturnExtraOption() {
        Long id = 1L;
        ExtraOption expectedExtraOption = ExtraOption.builder().build();
        expectedExtraOption.setId(id);

        when(extraOptionRepository.findById(id)).thenReturn(Optional.of(expectedExtraOption));

        ExtraOption actualExtraOption = extraOptionService.findById(id);

        assertNotNull(actualExtraOption);
        assertEquals(expectedExtraOption, actualExtraOption);
    }

    @Test
    void findAll_ShouldReturnListOfExtraOptions() {
        List<ExtraOption> expectedExtraOptions = new ArrayList<>();
        expectedExtraOptions.add(ExtraOption.builder().build());
        expectedExtraOptions.add(ExtraOption.builder().build());

        when(extraOptionRepository.findAll()).thenReturn(expectedExtraOptions);

        List<ExtraOption> actualExtraOptions = extraOptionService.findAll();

        assertNotNull(actualExtraOptions);
        assertEquals(expectedExtraOptions.size(), actualExtraOptions.size());
        assertTrue(expectedExtraOptions.containsAll(actualExtraOptions) && actualExtraOptions.containsAll(expectedExtraOptions));
    }

}
