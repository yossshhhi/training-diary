package kz.yossshhhi.dao.repository;

import kz.yossshhhi.model.ExtraOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Extra Option Repository Tests")
class ExtraOptionRepositoryTest {
    @Mock
    private ExtraOptionRepository extraOptionRepository;

    @Test
    @DisplayName("Save should persist extra option and return it with an ID")
    void testSave() {
        ExtraOption extraOption = new ExtraOption();
        extraOption.setWorkoutId(1L);
        extraOption.setTypeId(2L);
        extraOption.setValue(100);

        when(extraOptionRepository.save(any(ExtraOption.class))).thenReturn(extraOption);

        ExtraOption savedExtraOption = extraOptionRepository.save(extraOption);

        assertThat(savedExtraOption).isNotNull();
        assertThat(savedExtraOption.getWorkoutId()).isEqualTo(1L);
        assertThat(savedExtraOption.getTypeId()).isEqualTo(2L);
        assertThat(savedExtraOption.getValue()).isEqualTo(100);

        verify(extraOptionRepository).save(extraOption);
    }

    @Test
    @DisplayName("findAllByWorkoutId should return list of extra options")
    void testFindAllByWorkoutId() {
        Long workoutId = 1L;
        List<ExtraOption> expectedExtraOptions = Arrays.asList(new ExtraOption(), new ExtraOption());

        when(extraOptionRepository.findAllByWorkoutId(workoutId)).thenReturn(expectedExtraOptions);

        List<ExtraOption> actualExtraOptions = extraOptionRepository.findAllByWorkoutId(workoutId);

        assertThat(actualExtraOptions).isNotNull().hasSize(2);
        verify(extraOptionRepository).findAllByWorkoutId(workoutId);
    }

    @Test
    @DisplayName("deleteAll should remove all specified extra options")
    void testDeleteAll() {
        List<ExtraOption> extraOptions = Arrays.asList(
                mock(ExtraOption.class),
                mock(ExtraOption.class)
        );

        doNothing().when(extraOptionRepository).deleteAll(extraOptions);

        extraOptionRepository.deleteAll(extraOptions);

        verify(extraOptionRepository).deleteAll(extraOptions);
    }
}