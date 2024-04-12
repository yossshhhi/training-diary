package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionRepository;
import kz.yossshhhi.dao.repository.WorkoutDiaryRepository;
import kz.yossshhhi.dao.repository.WorkoutRepository;
import kz.yossshhhi.dao.repository.WorkoutTypeRepository;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.model.WorkoutDiary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutDiaryServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;
    @Mock
    private WorkoutDiaryRepository diaryRepository;
    @Mock
    private ExtraOptionRepository extraOptionRepository;
    @Mock
    private WorkoutTypeRepository workoutTypeRepository;

    private WorkoutDiaryService workoutDiaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        workoutDiaryService = new WorkoutDiaryService(workoutRepository, diaryRepository, extraOptionRepository, workoutTypeRepository);
    }

    @Test
    void create_WorkoutDoesNotExistInDiary_ShouldCreateWorkout() {
        Long userId = 1L;
        Workout workout = new Workout();
        workout.setCreatedAt(LocalDate.now());
        workout.setWorkoutTypeId(1L);
        WorkoutDiary workoutDiary = WorkoutDiary.builder()
                .userId(userId)
                .workoutList(new ArrayList<>())
                .build();

        when(diaryRepository.findByUserId(userId)).thenReturn(Optional.of(workoutDiary));
        when(diaryRepository.existsByDateAndWorkoutTypeId(workoutDiary.getId(), workout.getCreatedAt(), workout.getWorkoutTypeId())).thenReturn(false);
        when(workoutRepository.save(workout)).thenReturn(workout);

        Workout createdWorkout = workoutDiaryService.create(workout, userId);

        assertNotNull(createdWorkout);
        verify(workoutRepository, times(1)).save(workout);
        assertEquals(workoutDiary.getWorkoutList().size(), 1);
        assertEquals(workoutDiary.getWorkoutList().get(0), createdWorkout);
    }

    @Test
    void findAllByUserId_UserExistsWithWorkouts_ShouldReturnWorkoutList() {
        Long userId = 1L;
        WorkoutDiary workoutDiary = new WorkoutDiary();
        workoutDiary.setWorkoutList(List.of(new Workout(), new Workout()));

        when(diaryRepository.findByUserId(userId)).thenReturn(Optional.of(workoutDiary));

        List<Workout> result = workoutDiaryService.findAllByUserId(userId);

        assertEquals(workoutDiary.getWorkoutList(), result);
    }

    @Test
    void findAll_ReturnsListOfWorkouts() {
        List<Workout> expectedWorkouts = Arrays.asList(
                Workout.builder().id(1L).build(),
                Workout.builder().id(2L).build(),
                Workout.builder().id(3L).build()
        );
        when(workoutRepository.findAll()).thenReturn(expectedWorkouts);

        List<Workout> actualWorkouts = workoutDiaryService.findAll();

        assertEquals(expectedWorkouts.size(), actualWorkouts.size());
        assertEquals(expectedWorkouts, actualWorkouts);
        verify(workoutRepository, times(1)).findAll();
    }

    @Test
    void findById_WorkoutExists_ShouldReturnWorkout() {
        Long workoutId = 1L;
        Workout workout = Workout.builder().build();
        workout.setId(workoutId);

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));

        Workout foundWorkout = workoutDiaryService.findById(workoutId);

        assertNotNull(foundWorkout);
        assertEquals(workout, foundWorkout);
    }

    @Test
    void getStatistics_NoWorkoutsInSpecifiedDays_ShouldReturnZero() {
        Integer days = 7;
        Long userId = 1L;
        when(diaryRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Map<String, Integer> statistics = workoutDiaryService.getStatistics(days, userId);

        assertEquals(4, statistics.size());
    }
}