package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionRepository;
import kz.yossshhhi.model.ExtraOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Extra Option Service Tests")
class ExtraOptionServiceTest {

    @Mock
    private ExtraOptionRepository extraOptionRepository;

    @InjectMocks
    private ExtraOptionService extraOptionService;

    @Test
    @DisplayName("Save Extra Option")
    void save_ShouldReturnSavedExtraOption() {
        ExtraOption extraOption = ExtraOption.builder()
                .id(1L)
                .build();

        when(extraOptionRepository.save(extraOption)).thenReturn(extraOption);

        ExtraOption savedExtraOption = extraOptionService.save(extraOption);

        assertEquals(extraOption, savedExtraOption);
        verify(extraOptionRepository, times(1)).save(extraOption);
        verifyNoMoreInteractions(extraOptionRepository);
    }

    @Test
    @DisplayName("Delete All Extra Options")
    void deleteAll_ShouldNotReturnAnyValue() {
        List<ExtraOption> extraOptions = List.of(
                mock(ExtraOption.class),
                mock(ExtraOption.class)
        );

        extraOptionService.deleteAll(extraOptions);

        verify(extraOptionRepository, times(1)).deleteAll(extraOptions);
        verifyNoMoreInteractions(extraOptionRepository);
    }

}