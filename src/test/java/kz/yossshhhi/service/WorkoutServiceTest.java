package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionRepository;
import kz.yossshhhi.dao.repository.WorkoutRepository;
import kz.yossshhhi.model.AggregateWorkoutData;
import kz.yossshhhi.model.Workout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("Workout Service Tests")
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;
    @Mock
    private ExtraOptionRepository extraOptionRepository;

    private WorkoutService workoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        workoutService = new WorkoutService(workoutRepository, extraOptionRepository);
    }

    @Test
    @DisplayName("Creating Workout When Not Exists Should Save and Return Workout")
    void create_WorkoutDoesNotExist_ShouldSaveAndReturnWorkout() {
        Workout newWorkout = Workout.builder()
                .userId(1L)
                .createdAt(LocalDate.now())
                .workoutTypeId(1L)
                .extraOptions(new ArrayList<>())
                .build();

        when(workoutRepository.existsByUserIdAndDateAndWorkoutTypeId(newWorkout.getUserId(), newWorkout.getCreatedAt(), newWorkout.getWorkoutTypeId())).thenReturn(false);
        when(workoutRepository.save(newWorkout)).thenReturn(newWorkout);

        Workout createdWorkout = workoutService.create(newWorkout);

        assertNotNull(createdWorkout);
        assertEquals(newWorkout, createdWorkout);
        verify(workoutRepository, times(1)).existsByUserIdAndDateAndWorkoutTypeId(newWorkout.getUserId(), newWorkout.getCreatedAt(), newWorkout.getWorkoutTypeId());
        verify(workoutRepository, times(1)).save(newWorkout);
        verifyNoMoreInteractions(workoutRepository);
    }

    @Test
    @DisplayName("Finding All Workouts By User ID When User Exists With Workouts Should Return Workout List")
    void findAllByUserId_UserExistsWithWorkouts_ShouldReturnWorkoutList() {
        Long userId = 1L;
        List<Workout> expectedWorkouts = Arrays.asList(
                Workout.builder().id(1L).build(),
                Workout.builder().id(2L).build(),
                Workout.builder().id(3L).build()
        );

        when(workoutRepository.findAllByUserId(userId)).thenReturn(expectedWorkouts);

        List<Workout> result = workoutService.findAllByUserId(userId);

        assertEquals(expectedWorkouts, result);
        verify(workoutRepository, times(1)).findAllByUserId(userId);
        verifyNoMoreInteractions(workoutRepository);
    }

    @Test
    @DisplayName("Finding All Workouts Should Return List of Workouts")
    void findAll_ReturnsListOfWorkouts() {
        List<Workout> expectedWorkouts = Arrays.asList(
                Workout.builder().id(1L).build(),
                Workout.builder().id(2L).build(),
                Workout.builder().id(3L).build()
        );
        when(workoutRepository.findAll()).thenReturn(expectedWorkouts);

        List<Workout> actualWorkouts = workoutService.findAll();

        assertEquals(expectedWorkouts.size(), actualWorkouts.size());
        assertEquals(expectedWorkouts, actualWorkouts);
        verify(workoutRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Finding Workout By ID When Workout Exists Should Return Workout")
    void findById_WorkoutExists_ShouldReturnWorkout() {
        Long workoutId = 1L;
        Workout workout = Workout.builder().build();
        workout.setId(workoutId);

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));

        Workout foundWorkout = workoutService.findById(workoutId);

        assertNotNull(foundWorkout);
        assertEquals(workout, foundWorkout);
    }

    @Test
    @DisplayName("Get Statistics for the Last n Days")
    void getStatistics() {
        int days = 7;
        Long userId = 1L;
        AggregateWorkoutData data = new AggregateWorkoutData();
        data.setWorkoutCount(1L);
        data.setTotalDuration(120L);
        data.setTotalBurnedCalories(500L);

        when(workoutRepository.getAggregateDataByUserIdAndAfterDate(userId, LocalDate.now().minusDays(days)))
                .thenReturn(data);

        AggregateWorkoutData statistics = workoutService.getStatistics(userId, days);

        assertEquals(data.getWorkoutCount(), statistics.getWorkoutCount());
        verify(workoutRepository, times(1)).getAggregateDataByUserIdAndAfterDate(userId, LocalDate.now().minusDays(days));
        verifyNoMoreInteractions(workoutRepository);
    }
}