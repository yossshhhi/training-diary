package kz.yossshhhi.dao.repository;

import kz.yossshhhi.container.TestContainerInitializer;
import kz.yossshhhi.dao.WorkoutDAO;
import kz.yossshhhi.model.AggregateWorkoutData;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Workout Repository Tests")
class WorkoutRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static WorkoutRepository workoutRepository;
    private static Workout save;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer.start();
        TestContainerInitializer.initializeDatabase(postgreSQLContainer);
        DatabaseManager databaseManager = TestContainerInitializer.databaseManager(postgreSQLContainer);
        workoutRepository = new WorkoutDAO(databaseManager, new ResultSetMapper<>(Workout.class),
                new ResultSetMapper<>(AggregateWorkoutData.class));

        Workout workout1 = Workout.builder().userId(1L).workoutTypeId(1L).duration(100).burnedCalories(200).createdAt(LocalDate.now()).build();
        Workout workout2 = Workout.builder().userId(1L).workoutTypeId(2L).duration(100).burnedCalories(200).createdAt(LocalDate.now()).build();
        save = workoutRepository.save(workout1);
        workoutRepository.save(workout2);
    }

    @AfterAll
    static void destroy(){
        postgreSQLContainer.stop();
    }

    @Test
    @Order(1)
    @DisplayName("Find By ID - Existing ID Should Return Workout")
    void testFindById_existingId_shouldReturnWorkout() {
        Optional<Workout> foundWorkout = workoutRepository.findById(save.getId());
        assertTrue(foundWorkout.isPresent());
        assertEquals(save.getId(), foundWorkout.get().getId());
    }

    @Test
    @Order(2)
    @DisplayName("Save - Should Return Workout")
    void testSave_shouldReturnWorkout() {
        Workout workout = Workout.builder()
                .userId(1L)
                .workoutTypeId(3L)
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
    @Order(3)
    @DisplayName("Delete - Should Delete Workout")
    void testDelete() {
        Optional<Workout> foundWorkout = workoutRepository.findById(save.getId());

        assertTrue(foundWorkout.isPresent());

        workoutRepository.delete(save.getId());
        foundWorkout = workoutRepository.findById(save.getId());

        assertTrue(foundWorkout.isEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("Find All - Should Return All Workouts")
    void testFindAll() {
        List<Workout> allWorkouts = workoutRepository.findAll();
        assertFalse(allWorkouts.isEmpty());
        assertEquals(2, allWorkouts.size());
    }

    @Test
    @Order(5)
    @DisplayName("Find All By User ID - Should Return Workouts")
    void testFindAllByUserId() {
        List<Workout> workouts = workoutRepository.findAllByUserId(1L);
        assertEquals(2, workouts.size());
    }

    @Test
    @Order(6)
    @DisplayName("Exists By User ID And Date And Workout Type ID - Should Return True")
    void testExistsByUserIdAndDateAndWorkoutTypeId() {
        boolean exists = workoutRepository.existsByUserIdAndDateAndWorkoutTypeId(1L, LocalDate.now(), 1L);
        assertTrue(exists);
    }

    @Test
    @Order(7)
    @DisplayName("Get Aggregate Data By User ID And After Date - Should Return Aggregate Data")
    void testGetAggregateDataByUserIdAndAfterDate() {
        AggregateWorkoutData aggregateData = workoutRepository.getAggregateDataByUserIdAndAfterDate(1L, LocalDate.now().minusDays(1));
        assertNotNull(aggregateData);
        assertEquals(2, aggregateData.getWorkoutCount());
        assertEquals(200, aggregateData.getTotalDuration());
        assertEquals(400, aggregateData.getTotalBurnedCalories());
    }
}