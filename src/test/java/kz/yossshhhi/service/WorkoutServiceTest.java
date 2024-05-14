package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionRepository;
import kz.yossshhhi.dao.repository.WorkoutRepository;
import kz.yossshhhi.dto.AggregateWorkoutDataDTO;
import kz.yossshhhi.dto.WorkoutDTO;
import kz.yossshhhi.mapper.AggregateWorkoutDataMapper;
import kz.yossshhhi.mapper.WorkoutMapper;
import kz.yossshhhi.model.AggregateWorkoutData;
import kz.yossshhhi.model.Workout;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Workout Service Tests")
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;
    @Mock
    private ExtraOptionRepository extraOptionRepository;
    @Mock
    private WorkoutMapper workoutMapper;
    @Mock
    private AggregateWorkoutDataMapper aggregateWorkoutDataMapper;

    @InjectMocks
    private WorkoutService workoutService;

    @Test
    @DisplayName("Creating Workout When Not Exists Should Save and Return Workout")
    void create_WorkoutDoesNotExist_ShouldSaveAndReturnWorkout() {
        WorkoutDTO workoutDTO = mock(WorkoutDTO.class);
        Workout newWorkout = mock(Workout.class);

        when(workoutMapper.toEntity(workoutDTO)).thenReturn(newWorkout);
        when(workoutRepository.existsByUserIdAndDateAndWorkoutTypeId(newWorkout.getUserId(), newWorkout.getCreatedAt(), newWorkout.getWorkoutTypeId())).thenReturn(false);
        when(workoutRepository.save(newWorkout)).thenReturn(newWorkout);

        Workout createdWorkout = workoutService.create(workoutDTO, 1L);

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

        List<WorkoutDTO> result = workoutService.findAllByUserId(userId);

        assertEquals(expectedWorkouts.size(), result.size());
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

        List<WorkoutDTO> actualWorkouts = workoutService.findAll();

        assertEquals(expectedWorkouts.size(), actualWorkouts.size());
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
        AggregateWorkoutData data = mock(AggregateWorkoutData.class);
        AggregateWorkoutDataDTO dto = mock(AggregateWorkoutDataDTO.class);

        when(workoutRepository.getAggregateDataByUserIdAndAfterDate(userId, LocalDate.now().minusDays(days)))
                .thenReturn(data);
        when(aggregateWorkoutDataMapper.toDTO(data)).thenReturn(dto);

        AggregateWorkoutDataDTO statistics = workoutService.getStatistics(userId, days);

        assertEquals(data.getWorkoutCount(), statistics.workoutCount());
        verify(workoutRepository, times(1)).getAggregateDataByUserIdAndAfterDate(userId, LocalDate.now().minusDays(days));
        verifyNoMoreInteractions(workoutRepository);
    }
}