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
import java.util.ArrayList;
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
    private static List<Workout> save;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer.start();
        TestContainerInitializer.initializeDatabase(postgreSQLContainer);
        DatabaseManager databaseManager = TestContainerInitializer.databaseManager(postgreSQLContainer);
        workoutRepository = new WorkoutDAO(databaseManager, new ResultSetMapper<>(Workout.class),
                new ResultSetMapper<>(AggregateWorkoutData.class));

        save = new ArrayList<>();
        Workout workout1 = Workout.builder().userId(1L).workoutTypeId(1L).duration(100).burnedCalories(200).createdAt(LocalDate.now()).build();
        Workout workout2 = Workout.builder().userId(1L).workoutTypeId(2L).duration(100).burnedCalories(200).createdAt(LocalDate.now()).build();
        save.add(workoutRepository.save(workout1));
        save.add(workoutRepository.save(workout2));
    }

    @AfterAll
    static void destroy(){
        postgreSQLContainer.stop();
    }

    @Test
    @DisplayName("Find By ID - Existing ID Should Return Workout")
    void testFindById_existingId_shouldReturnWorkout() {
        Optional<Workout> foundWorkout = workoutRepository.findById(save.get(0).getId());
        assertTrue(foundWorkout.isPresent());
        assertEquals(save.get(0).getId(), foundWorkout.get().getId());
    }

    @Test
    @DisplayName("Save - Should Return Workout")
    void testSave_shouldReturnWorkout() {
        Workout workout = Workout.builder().userId(1L).workoutTypeId(3L).duration(100).burnedCalories(200).createdAt(LocalDate.now()).build();

        Workout saved = workoutRepository.save(workout);
        save.add(saved);

        assertNotNull(saved.getId());
        assertEquals(workout.getBurnedCalories(), saved.getBurnedCalories());
        assertEquals(workout.getWorkoutTypeId(), saved.getWorkoutTypeId());
    }

    @Test
    @DisplayName("Delete - Should Delete Workout")
    void testDelete() {
        Optional<Workout> foundWorkout = workoutRepository.findById(save.get(0).getId());

        assertTrue(foundWorkout.isPresent());

        workoutRepository.delete(save.get(0).getId());
        foundWorkout = workoutRepository.findById(save.get(0).getId());
        save.remove(0);

        assertTrue(foundWorkout.isEmpty());
    }

    @Test
    @DisplayName("Find All - Should Return All Workouts")
    void testFindAll() {
        List<Workout> allWorkouts = workoutRepository.findAll();
        assertEquals(save.size(), allWorkouts.size());
    }

    @Test
    @DisplayName("Find All By User ID - Should Return Workouts")
    void testFindAllByUserId() {
        List<Workout> workouts = workoutRepository.findAllByUserId(1L);
        assertEquals(save.size(), workouts.size());
    }

    @Test
    @DisplayName("Exists By User ID And Date And Workout Type ID - Should Return True")
    void testExistsByUserIdAndDateAndWorkoutTypeId() {
        boolean exists = workoutRepository.existsByUserIdAndDateAndWorkoutTypeId(1L, LocalDate.now(), 2L);
        assertTrue(exists);
    }

    @Test
    @DisplayName("Get Aggregate Data By User ID And After Date - Should Return Aggregate Data")
    void testGetAggregateDataByUserIdAndAfterDate() {
        AggregateWorkoutData aggregateData = workoutRepository.getAggregateDataByUserIdAndAfterDate(1L, LocalDate.now().minusDays(1));
        assertNotNull(aggregateData);
        assertEquals(save.size(), aggregateData.getWorkoutCount());
        assertEquals(save.stream().mapToInt(Workout::getDuration).sum(), aggregateData.getTotalDuration());
        assertEquals(save.stream().mapToInt(Workout::getBurnedCalories).sum(), aggregateData.getTotalBurnedCalories());
    }
}