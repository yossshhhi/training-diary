package kz.yossshhhi.dao.repository;

import kz.yossshhhi.model.WorkoutType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Workout Type Repository Tests")
class WorkoutTypeRepositoryTest {

    @Mock
    private WorkoutTypeRepository workoutTypeRepository;

    @Test
    @DisplayName("Save should return saved WorkoutType")
    void testSave_shouldReturnSavedWorkoutType() {
        WorkoutType newWorkoutType = new WorkoutType(null, "Dance");
        WorkoutType savedWorkoutType = new WorkoutType(3L, "Dance");
        when(workoutTypeRepository.save(newWorkoutType)).thenReturn(savedWorkoutType);

        WorkoutType result = workoutTypeRepository.save(newWorkoutType);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Dance");
    }

    @Test
    @DisplayName("Find by ID with existing ID should return WorkoutType")
    void testFindById_existingId_shouldReturnWorkoutType() {
        WorkoutType expectedWorkoutType = new WorkoutType(1L, "Cardio");
        when(workoutTypeRepository.findById(1L)).thenReturn(Optional.of(expectedWorkoutType));

        Optional<WorkoutType> result = workoutTypeRepository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result).contains(expectedWorkoutType);
    }

    @Test
    @DisplayName("Find by name with existing name should return WorkoutType")
    void testFindByName_existingName_shouldReturnWorkoutType() {
        WorkoutType expectedWorkoutType = new WorkoutType(1L, "Cardio");
        when(workoutTypeRepository.findByName("Cardio")).thenReturn(Optional.of(expectedWorkoutType));

        Optional<WorkoutType> result = workoutTypeRepository.findByName("Cardio");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Cardio");
    }

    @Test
    @DisplayName("Find all should return all WorkoutTypes")
    void testFindAll_shouldReturnAllWorkoutTypes() {
        List<WorkoutType> expectedTypes = List.of(
                new WorkoutType(1L, "Cardio"),
                new WorkoutType(2L, "Strength")
        );
        when(workoutTypeRepository.findAll()).thenReturn(expectedTypes);

        List<WorkoutType> results = workoutTypeRepository.findAll();

        assertThat(results).hasSize(2);
        assertThat(results).extracting(WorkoutType::getName).containsExactly("Cardio", "Strength");
    }
}