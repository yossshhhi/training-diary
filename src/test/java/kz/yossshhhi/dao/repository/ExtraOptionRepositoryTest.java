package kz.yossshhhi.dao.repository;

import kz.yossshhhi.container.TestContainerInitializer;
import kz.yossshhhi.dao.ExtraOptionDAO;
import kz.yossshhhi.dao.WorkoutDAO;
import kz.yossshhhi.model.AggregateWorkoutData;
import kz.yossshhhi.model.ExtraOption;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DisplayName("Extra Option Repository Tests")
class ExtraOptionRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static ExtraOptionRepository extraOptionRepository;
    private static Workout savedWorkout;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer.start();
        TestContainerInitializer.initializeDatabase(postgreSQLContainer);
        DatabaseManager databaseManager = TestContainerInitializer.databaseManager(postgreSQLContainer);
        extraOptionRepository = new ExtraOptionDAO(databaseManager, new ResultSetMapper<>(ExtraOption.class));
        WorkoutRepository workoutRepository = new WorkoutDAO(databaseManager, new ResultSetMapper<>(Workout.class),
                new ResultSetMapper<>(AggregateWorkoutData.class));

        Workout workout = Workout.builder()
                .userId(1L)
                .workoutTypeId(1L)
                .duration(100)
                .burnedCalories(200)
                .createdAt(LocalDate.now())
                .build();

        savedWorkout = workoutRepository.save(workout);
        extraOptionRepository.save(ExtraOption.builder()
                .typeId(1L)
                .workoutId(savedWorkout.getId())
                .value(100)
                .build());
    }

    @AfterAll
    static void destroy(){
        postgreSQLContainer.stop();
    }

    @Test
    @DisplayName("Save Extra Option and Find All by Workout ID - Should Return List of Extra Options")
    void testSaveAndFindAllByWorkoutId_ShouldReturnListOfExtraOptions() {
        ExtraOption extraOption = ExtraOption.builder()
                .typeId(2L)
                .workoutId(savedWorkout.getId())
                .value(100)
                .build();

        extraOptionRepository.save(extraOption);

        assertThat(extraOptionRepository.findAllByWorkoutId(savedWorkout.getId()))
                .anyMatch(option -> option.getTypeId().equals(extraOption.getTypeId()))
                .hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Delete All Extra Options - Should Delete All Extra Options")
    void deleteAll_ShouldDeleteAllExtraOptions() {
        List<ExtraOption> extraOptions = extraOptionRepository.findAllByWorkoutId(savedWorkout.getId());

        assertFalse(extraOptions.isEmpty());

        extraOptionRepository.deleteAll(extraOptions);

        extraOptions = extraOptionRepository.findAllByWorkoutId(savedWorkout.getId());

        assertTrue(extraOptions.isEmpty());
    }
}