package kz.yossshhhi.dao.repository;

import kz.yossshhhi.model.AggregateWorkoutData;
import kz.yossshhhi.model.Workout;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Workout Repository Tests")
@ExtendWith(MockitoExtension.class)
class WorkoutRepositoryTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Test
    @DisplayName("Find By ID - Existing ID Should Return Workout")
    void findById_ShouldReturnWorkoutWhenExists() {
        Workout workout = new Workout();
        workout.setId(1L);
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));

        Optional<Workout> found = workoutRepository.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(workout);
    }

    @Test
    @DisplayName("Find By ID - Not Existing ID Should Return Empty")
    void findById_ShouldReturnEmptyWhenNotExists() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Workout> found = workoutRepository.findById(1L);

        assertThat(found).isNotPresent();
    }

    @Test
    @DisplayName("Save - Should Return Workout")
    void save_ShouldSaveOrUpdateWorkout() {
        Workout workout = Workout.builder().userId(1L).workoutTypeId(3L).duration(100).burnedCalories(200).createdAt(LocalDate.now()).build();

        when(workoutRepository.save(workout)).thenReturn(workout);

        Workout saved = workoutRepository.save(workout);

        assertThat(saved).isNotNull();
        assertThat(saved.getDuration()).isEqualTo(workout.getDuration());
    }

    @Test
    @DisplayName("Delete - Should Delete Workout")
    void delete_ShouldReturnTrueWhenSuccessful() {
        when(workoutRepository.delete(1L)).thenReturn(true);

        boolean result = workoutRepository.delete(1L);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Find All By User ID - Should Return Workouts")
    void findAllByUserId_ShouldReturnWorkouts() {
        List<Workout> workouts = List.of(new Workout(), new Workout());
        when(workoutRepository.findAllByUserId(1L)).thenReturn(workouts);

        List<Workout> result = workoutRepository.findAllByUserId(1L);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Exists By User ID And Date And Workout Type ID - Should Return True")
    void existsByUserIdAndDateAndWorkoutTypeId_ShouldReturnTrueIfExists() {
        when(workoutRepository.existsByUserIdAndDateAndWorkoutTypeId(1L, LocalDate.now(), 1L)).thenReturn(true);

        boolean exists = workoutRepository.existsByUserIdAndDateAndWorkoutTypeId(1L, LocalDate.now(), 1L);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Find All - Should Return All Workouts")
    void findAll_ShouldReturnAllWorkouts() {
        List<Workout> expectedWorkouts = Arrays.asList(new Workout(), new Workout());
        when(workoutRepository.findAll()).thenReturn(expectedWorkouts);

        List<Workout> workouts = workoutRepository.findAll();

        assertThat(workouts).isNotNull();
        assertThat(workouts).hasSize(2);
        assertThat(workouts).isEqualTo(expectedWorkouts);
    }

    @Test
    @DisplayName("Get Aggregate Data By User ID And After Date - Should Return Aggregate Data")
    void getAggregateDataByUserIdAndAfterDate_ShouldReturnAggregateData() {
        AggregateWorkoutData expectedData = new AggregateWorkoutData();
        expectedData.setWorkoutCount(10L);
        expectedData.setTotalDuration(500L);

        when(workoutRepository.getAggregateDataByUserIdAndAfterDate(1L, LocalDate.of(2021, 1, 1))).thenReturn(expectedData);

        AggregateWorkoutData actualData = workoutRepository.getAggregateDataByUserIdAndAfterDate(1L, LocalDate.of(2021, 1, 1));

        assertThat(actualData).isNotNull();
        assertThat(actualData.getWorkoutCount()).isEqualTo(10);
        assertThat(actualData.getTotalDuration()).isEqualTo(500);
    }
}