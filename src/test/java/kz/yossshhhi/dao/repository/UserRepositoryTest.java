package kz.yossshhhi.dao.repository;

import kz.yossshhhi.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Repository Tests")
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Should return user when user ID is found")
    void findById_Found() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setUsername("new_user");

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        Optional<User> actualUser = userRepository.findById(userId);

        assertThat(actualUser).isPresent();
        assertThat(actualUser.get()).isEqualTo(expectedUser);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should return empty when user ID is not found")
    void findById_NotFound() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findById(userId);

        assertThat(result).isNotPresent();
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should return user when username is found")
    void findByUsername_Found() {
        String username = "new_user";
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        Optional<User> actualUser = userRepository.findByUsername(username);

        assertThat(actualUser).isPresent();
        assertThat(actualUser.get()).isEqualTo(expectedUser);
        verify(userRepository).findByUsername(username);
    }

    @Test
    @DisplayName("Should save and return the user")
    void save_User() {
        User newUser = new User();
        newUser.setUsername("new_user");

        when(userRepository.save(newUser)).thenReturn(newUser);

        User savedUser = userRepository.save(newUser);

        assertThat(savedUser).isEqualTo(newUser);
        verify(userRepository).save(newUser);
    }
}