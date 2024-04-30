package kz.yossshhhi.dao.repository;

import kz.yossshhhi.container.TestContainerInitializer;
import kz.yossshhhi.dao.WorkoutTypeDAO;
import kz.yossshhhi.model.WorkoutType;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DisplayName("Workout Type Repository Tests")
class WorkoutTypeRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static WorkoutTypeRepository workoutTypeRepository;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer.start();
        TestContainerInitializer.initializeDatabase(postgreSQLContainer);
        DatabaseManager databaseManager = TestContainerInitializer.databaseManager(postgreSQLContainer);
        workoutTypeRepository = new WorkoutTypeDAO(databaseManager, new ResultSetMapper<>(WorkoutType.class));
    }

    @AfterAll
    static void destroy(){
        postgreSQLContainer.stop();
    }

    @Test
    @DisplayName("Save should return saved WorkoutType")
    void testSave_shouldReturnSavedWorkoutType() {
        WorkoutType workoutType = WorkoutType.builder()
                .name("Dance")
                .build();
        WorkoutType savedWorkoutType = workoutTypeRepository.save(workoutType);
        assertNotNull(savedWorkoutType.getId());
        assertEquals(workoutType.getName(), savedWorkoutType.getName());
    }

    @Test
    @DisplayName("Find by ID with existing ID should return WorkoutType")
    void testFindById_existingId_shouldReturnWorkoutType() {
        Optional<WorkoutType> foundWorkoutType = workoutTypeRepository.findById(1L);
        assertTrue(foundWorkoutType.isPresent());
        assertEquals(1L, foundWorkoutType.get().getId());
        assertEquals("Cardio", foundWorkoutType.get().getName());
    }

    @Test
    @DisplayName("Find by name with existing name should return WorkoutType")
    void testFindByName_existingName_shouldReturnWorkoutType() {
        Optional<WorkoutType> foundWorkoutType = workoutTypeRepository.findByName("Cardio");
        assertTrue(foundWorkoutType.isPresent());
        assertEquals(1L, foundWorkoutType.get().getId());
        assertEquals("Cardio", foundWorkoutType.get().getName());
    }

    @Test
    @DisplayName("Find all should return all WorkoutTypes")
    void testFindAll_shouldReturnAllWorkoutTypes() {
        assertThat(workoutTypeRepository.findAll())
                .anyMatch(type -> type.getName().equals("Power training"))
                .anyMatch(type -> type.getName().equals("Dance workouts"))
                .anyMatch(type -> type.getName().equals("Yoga"))
                .size().isGreaterThanOrEqualTo(6);
    }
}