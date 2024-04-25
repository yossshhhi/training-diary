package kz.yossshhhi.dao.repository;

import kz.yossshhhi.container.TestContainerInitializer;
import kz.yossshhhi.dao.ExtraOptionDAO;
import kz.yossshhhi.dao.WorkoutDAO;
import kz.yossshhhi.model.AggregateWorkoutData;
import kz.yossshhhi.model.ExtraOption;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
@DisplayName("Extra Option Repository Tests")
class ExtraOptionRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static ExtraOptionRepository extraOptionRepository;
    private static WorkoutRepository workoutRepository;

    @BeforeEach
    void setUp() {
        TestContainerInitializer.initializeDatabase(postgreSQLContainer);
        DatabaseManager databaseManager = TestContainerInitializer.databaseManager(postgreSQLContainer);
        extraOptionRepository = new ExtraOptionDAO(databaseManager, new ResultSetMapper<>(ExtraOption.class));
        workoutRepository = new WorkoutDAO(databaseManager, new ResultSetMapper<>(Workout.class),
                new ResultSetMapper<>(AggregateWorkoutData.class));
    }

    @Test
    @DisplayName("Save Extra Option and Find All by Workout ID - Should Return List of Extra Options")
    void testSaveAndFindAllByWorkoutId_ShouldReturnListOfExtraOptions() {
        Workout workout = Workout.builder()
                .userId(1L)
                .workoutTypeId(1L)
                .duration(100)
                .burnedCalories(200)
                .createdAt(LocalDate.now())
                .build();

        Workout savedWorkout = workoutRepository.save(workout);

        ExtraOption extraOption = ExtraOption.builder()
                .typeId(1L)
                .workoutId(savedWorkout.getId())
                .value(100)
                .build();

        extraOptionRepository.save(extraOption);
        List<ExtraOption> extraOptions = extraOptionRepository.findAllByWorkoutId(savedWorkout.getId());

        assertFalse(extraOptions.isEmpty());
        assertEquals(savedWorkout.getId(), extraOptions.get(0).getWorkoutId());
        assertEquals(extraOption.getTypeId(), extraOptions.get(0).getTypeId());
        assertEquals(1, extraOptions.size());
    }

    @Test
    @DisplayName("Delete All Extra Options - Should Delete All Extra Options")
    void deleteAll_ShouldDeleteAllExtraOptions() {
        Workout workout = Workout.builder()
                .userId(1L)
                .workoutTypeId(1L)
                .duration(100)
                .burnedCalories(200)
                .createdAt(LocalDate.now())
                .build();

        Workout savedWorkout = workoutRepository.save(workout);
        List<ExtraOption> extraOptions = extraOptionRepository.findAllByWorkoutId(savedWorkout.getId());

        assertEquals(0, extraOptions.size());

        ExtraOption extraOption1 = ExtraOption.builder()
                .typeId(1L)
                .workoutId(savedWorkout.getId())
                .value(100)
                .build();
        ExtraOption extraOption2 = ExtraOption.builder()
                .typeId(2L)
                .workoutId(savedWorkout.getId())
                .value(100)
                .build();

        extraOptionRepository.save(extraOption1);
        extraOptionRepository.save(extraOption2);

        extraOptions = extraOptionRepository.findAllByWorkoutId(savedWorkout.getId());

        assertEquals(2, extraOptions.size());

        extraOptionRepository.deleteAll(extraOptions);

        extraOptions = extraOptionRepository.findAllByWorkoutId(savedWorkout.getId());

        assertEquals(0, extraOptions.size());
    }
}