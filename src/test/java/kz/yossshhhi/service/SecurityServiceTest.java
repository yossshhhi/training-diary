package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.UserRepository;
import kz.yossshhhi.dto.AuthenticationDTO;
import kz.yossshhhi.exception.AuthenticationException;
import kz.yossshhhi.exception.RegistrationException;
import kz.yossshhhi.model.User;
import kz.yossshhhi.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Security Service Tests")
public class SecurityServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityService securityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Successful Registration")
    public void testRegistration_Successful() {
        AuthenticationDTO request = new AuthenticationDTO("username", "password");

        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User registeredUser = securityService.registration(request);

        assertNotNull(registeredUser);
        assertEquals("username", registeredUser.getUsername());
        assertEquals(Role.USER, registeredUser.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Registration: User Already Exists")
    public void testRegistration_UserAlreadyExists() {
        AuthenticationDTO request = new AuthenticationDTO("existinguser", "password");
        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(new User()));

        assertThrows(RegistrationException.class, () -> securityService.registration(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Successful Authentication")
    public void testAuthentication_Successful() {
        AuthenticationDTO request = new AuthenticationDTO("username", "password");
        String hashedPassword = hashPassword(request.getPassword());
        User existingUser = User.builder()
                .username("username")
                .password(hashedPassword)
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(existingUser));

        User authenticatedUser = securityService.authenticate(request);

        assertNotNull(authenticatedUser);
        assertEquals("username", authenticatedUser.getUsername());
        assertEquals(Role.USER, authenticatedUser.getRole());
    }

    @Test
    @DisplayName("Authentication: User Not Found")
    public void testAuthentication_UserNotFound() {
        AuthenticationDTO request = new AuthenticationDTO("username", "password");

        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> securityService.authenticate(request));
    }

    @Test
    @DisplayName("Authentication: Invalid Password")
    public void testAuthentication_InvalidPassword() {
        AuthenticationDTO request = new AuthenticationDTO("username", "password");
        User existingUser = User.builder()
                .username("username")
                .password("password")
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(existingUser));

        assertThrows(AuthenticationException.class, () -> securityService.authenticate(request));
    }

    private String hashPassword(String password) {
        try {
            Method method = SecurityService.class.getDeclaredMethod("hashPassword", String.class);
            method.setAccessible(true);
            return (String) method.invoke(securityService, password);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
