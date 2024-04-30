package kz.yossshhhi.dao.repository;

import kz.yossshhhi.container.TestContainerInitializer;
import kz.yossshhhi.dao.ExtraOptionTypeDAO;
import kz.yossshhhi.model.ExtraOptionType;
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
@DisplayName("Extra Option Type Repository Test")
class ExtraOptionTypeRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static ExtraOptionTypeRepository extraOptionTypeRepository;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer.start();
        TestContainerInitializer.initializeDatabase(postgreSQLContainer);
        DatabaseManager databaseManager = TestContainerInitializer.databaseManager(postgreSQLContainer);
        extraOptionTypeRepository = new ExtraOptionTypeDAO(databaseManager, new ResultSetMapper<>(ExtraOptionType.class));
    }

    @AfterAll
    static void destroy(){
        postgreSQLContainer.stop();
    }

    @Test
    @DisplayName("Save Extra Option Type")
    void testSave_shouldReturnSavedExtraOptionType() {
        ExtraOptionType extraOptionType = ExtraOptionType.builder()
                .name("Test")
                .build();

        ExtraOptionType savedExtraOptionType = extraOptionTypeRepository.save(extraOptionType);

        assertNotNull(savedExtraOptionType);
        assertEquals("Test", savedExtraOptionType.getName());

    }

    @Test
    @DisplayName("Find By ID - Existing ID")
    void findById_ExistingId_ShouldReturnExtraOptionType() {
        Long id = 1L;

        Optional<ExtraOptionType> result = extraOptionTypeRepository.findById(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    @DisplayName("Find By Name - Existing Name")
    void findByName_ExistingName_ShouldReturnExtraOptionType() {
        String name = "Repetitions";
        ExtraOptionType expectedType = ExtraOptionType.builder()
                .name(name)
                .build();

        Optional<ExtraOptionType> result = extraOptionTypeRepository.findByName(name);

        assertTrue(result.isPresent());
        assertEquals(expectedType.getName(), result.get().getName());
    }

    @Test
    @DisplayName("Find All Extra Option Types")
    void testFindAll_shouldReturnAllExtraOptionTypes() {
        assertThat(extraOptionTypeRepository.findAll())
                .anyMatch(type -> type.getName().equals("Repetitions"))
                .anyMatch(type -> type.getName().equals("Distance covered"))
                .hasSizeGreaterThanOrEqualTo(2);
    }
}