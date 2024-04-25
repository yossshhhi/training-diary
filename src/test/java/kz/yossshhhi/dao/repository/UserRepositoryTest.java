package kz.yossshhhi.dao.repository;

import kz.yossshhhi.container.TestContainerInitializer;
import kz.yossshhhi.dao.UserDAO;
import kz.yossshhhi.model.User;
import kz.yossshhhi.model.enums.Role;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DisplayName("User Repository Tests")
class UserRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static UserRepository userRepository;

    @BeforeEach
    void setUp() {
        TestContainerInitializer.initializeDatabase(postgreSQLContainer);
        DatabaseManager databaseManager = TestContainerInitializer.databaseManager(postgreSQLContainer);
        userRepository = new UserDAO(databaseManager, new ResultSetMapper<>(User.class));
    }

    @Test
    @DisplayName("Save User: Should return the saved user")
    void testSave_shouldReturnSavedUser() {
        User user = User.builder()
                .username("test")
                .password("test")
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @Test
    @DisplayName("Find By ID: Existing ID should return user")
    void testFindById_existingId_shouldReturnUser() {
        Long id = 1L;
        Optional<User> optionalUser = userRepository.findById(id);
        assertTrue(optionalUser.isPresent());
        User foundUser = optionalUser.get();
        assertEquals(id, foundUser.getId());
        assertEquals("invalid", foundUser.getUsername());
        assertEquals("invalid", foundUser.getPassword());
    }

    @Test
    @DisplayName("Find By Username: Existing username should return user")
    void testFindByUsername_existingUsername_shouldReturnUser() {
        String username = "invalid";
        Optional<User> optionalUser = userRepository.findByUsername(username);
        assertTrue(optionalUser.isPresent());
        User foundUser = optionalUser.get();
        assertEquals(username, foundUser.getUsername());
        assertEquals("invalid", foundUser.getPassword());
    }
}