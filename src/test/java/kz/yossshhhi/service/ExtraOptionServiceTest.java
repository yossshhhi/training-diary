package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionRepository;
import kz.yossshhhi.model.ExtraOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ExtraOptionServiceTest {

    @Mock
    private ExtraOptionRepository extraOptionRepository;

    @InjectMocks
    private ExtraOptionService extraOptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
        List<ExtraOption> extraOptions = new ArrayList<>();

        extraOptionService.deleteAll(extraOptions);

        verify(extraOptionRepository, times(1)).deleteAll(extraOptions);
        verifyNoMoreInteractions(extraOptionRepository);
    }

    @Test
    @DisplayName("Save All Extra Options By Workout")
    void saveAllByWorkout_ShouldReturnListOfSavedExtraOptions() {
        Long workoutId = 1L;
        Map<Long, Integer> inputMap = new HashMap<>();
        inputMap.put(1L, 10);
        inputMap.put(2L, 20);

        List<ExtraOption> expectedSavedExtraOptions = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : inputMap.entrySet()) {
            expectedSavedExtraOptions.add(ExtraOption.builder()
                    .workoutId(workoutId)
                    .typeId(entry.getKey())
                    .value(entry.getValue())
                    .id(null)
                    .build());
        }

        when(extraOptionRepository.save(any(ExtraOption.class))).thenAnswer(invocation -> {
            ExtraOption savedExtraOption = invocation.getArgument(0);
            savedExtraOption.setId(null);
            return savedExtraOption;
        });

        List<ExtraOption> result = extraOptionService.saveAllByWorkout(inputMap, workoutId);

        assertEquals(expectedSavedExtraOptions.size(), result.size());
        assertEquals(expectedSavedExtraOptions, result);
        verify(extraOptionRepository, times(inputMap.size())).save(any(ExtraOption.class));
        verifyNoMoreInteractions(extraOptionRepository);
    }

}