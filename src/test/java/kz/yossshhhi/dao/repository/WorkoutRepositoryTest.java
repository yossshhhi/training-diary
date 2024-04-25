package kz.yossshhhi.dao.repository;

import kz.yossshhhi.container.TestContainerInitializer;
import kz.yossshhhi.dao.WorkoutDAO;
import kz.yossshhhi.model.AggregateWorkoutData;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DisplayName("Workout Repository Tests")
class WorkoutRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static WorkoutRepository workoutRepository;

    @BeforeEach
    void setUp() {
        postgreSQLContainer.start();
        TestContainerInitializer.initializeDatabase(postgreSQLContainer);
        DatabaseManager databaseManager = TestContainerInitializer.databaseManager(postgreSQLContainer);
        workoutRepository = new WorkoutDAO(databaseManager, new ResultSetMapper<>(Workout.class),
                new ResultSetMapper<>(AggregateWorkoutData.class));
    }

    @AfterEach
    void destroy(){
        postgreSQLContainer.stop();
    }

    @Test
    @DisplayName("Find By ID - Existing ID Should Return Workout")
    void testFindById_existingId_shouldReturnWorkout() {
        Workout workout = Workout.builder()
                .userId(1L)
                .workoutTypeId(1L)
                .duration(100)
                .burnedCalories(200)
                .createdAt(LocalDate.now())
                .build();

        Workout savedWorkout = workoutRepository.save(workout);

        Optional<Workout> foundWorkout = workoutRepository.findById(savedWorkout.getId());
        assertTrue(foundWorkout.isPresent());
        assertEquals(savedWorkout.getId(), foundWorkout.get().getId());
    }

    @Test
    @DisplayName("Save - Should Return Workout")
    void testSave_shouldReturnWorkout() {
        Workout workout = Workout.builder()
                .userId(1L)
                .workoutTypeId(1L)
                .duration(100)
                .burnedCalories(200)
                .createdAt(LocalDate.now())
                .build();

        Workout savedWorkout = workoutRepository.save(workout);

        assertNotNull(savedWorkout.getId());
        assertEquals(workout.getBurnedCalories(), savedWorkout.getBurnedCalories());
        assertEquals(workout.getWorkoutTypeId(), savedWorkout.getWorkoutTypeId());
    }

    @Test
    @DisplayName("Delete - Should Delete Workout")
    void testDelete() {
        Workout workout = Workout.builder()
                .userId(1L)
                .workoutTypeId(1L)
                .duration(100)
                .burnedCalories(200)
                .createdAt(LocalDate.now())
                .build();

        Workout savedWorkout = workoutRepository.save(workout);
        Optional<Workout> foundWorkout = workoutRepository.findById(savedWorkout.getId());

        assertTrue(foundWorkout.isPresent());

        workoutRepository.delete(savedWorkout.getId());
        foundWorkout = workoutRepository.findById(savedWorkout.getId());

        assertTrue(foundWorkout.isEmpty());
    }

    @Test
    @DisplayName("Find All - Should Return All Workouts")
    void testFindAll() {
        Workout workout1 = Workout.builder().userId(1L).workoutTypeId(1L).duration(100).burnedCalories(200).createdAt(LocalDate.now()).build();
        Workout workout2 = Workout.builder().userId(1L).workoutTypeId(1L).duration(100).burnedCalories(200).createdAt(LocalDate.now()).build();
        workoutRepository.save(workout1);
        workoutRepository.save(workout2);

        List<Workout> allWorkouts = workoutRepository.findAll();
        assertFalse(allWorkouts.isEmpty());
        assertEquals(2, allWorkouts.size());
    }

    @Test
    @DisplayName("Find All By User ID - Should Return Workouts")
    void testFindAllByUserId() {
        Workout workout1 = Workout.builder().userId(1L).workoutTypeId(1L).duration(100).burnedCalories(200).createdAt(LocalDate.now()).build();
        Workout workout2 = Workout.builder().userId(1L).workoutTypeId(1L).duration(100).burnedCalories(200).createdAt(LocalDate.now()).build();
        workoutRepository.save(workout1);
        workoutRepository.save(workout2);

        List<Workout> workouts = workoutRepository.findAllByUserId(1L);
        assertEquals(2, workouts.size());
    }

    @Test
    @DisplayName("Exists By User ID And Date And Workout Type ID - Should Return True")
    void testExistsByUserIdAndDateAndWorkoutTypeId() {
        Workout workout = Workout.builder().userId(1L).workoutTypeId(1L).duration(100).burnedCalories(200).createdAt(LocalDate.now()).build();
        workoutRepository.save(workout);

        boolean exists = workoutRepository.existsByUserIdAndDateAndWorkoutTypeId(1L, LocalDate.now(), 1L);
        assertTrue(exists);
    }

    @Test
    @DisplayName("Get Aggregate Data By User ID And After Date - Should Return Aggregate Data")
    void testGetAggregateDataByUserIdAndAfterDate() {
        Workout workout1 = Workout.builder().userId(1L).workoutTypeId(1L).duration(100).burnedCalories(200).createdAt(LocalDate.now()).build();
        Workout workout2 = Workout.builder().userId(1L).workoutTypeId(1L).duration(100).burnedCalories(200).createdAt(LocalDate.now()).build();
        workoutRepository.save(workout1);
        workoutRepository.save(workout2);

        AggregateWorkoutData aggregateData = workoutRepository.getAggregateDataByUserIdAndAfterDate(1L, LocalDate.now().minusDays(1));
        assertNotNull(aggregateData);
        assertEquals(2, aggregateData.getWorkoutCount());
        assertEquals(workout1.getDuration() + workout2.getDuration(), aggregateData.getTotalDuration());
        assertEquals(workout1.getBurnedCalories() + workout2.getBurnedCalories(), aggregateData.getTotalBurnedCalories());
    }
}