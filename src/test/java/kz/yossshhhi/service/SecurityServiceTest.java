package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.UserRepository;
import kz.yossshhhi.dto.AuthenticationDTO;
import kz.yossshhhi.exception.AuthenticationException;
import kz.yossshhhi.exception.RegistrationException;
import kz.yossshhhi.model.User;
import kz.yossshhhi.model.enums.AuditAction;
import kz.yossshhhi.model.enums.AuditType;
import kz.yossshhhi.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Security Service Tests")
public class SecurityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditService auditService;

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
        assertEquals("password", registeredUser.getPassword());
        assertEquals(Role.USER, registeredUser.getRole());
    }

    @Test
    @DisplayName("Registration: User Already Exists")
    public void testRegistration_UserAlreadyExists() {
        AuthenticationDTO request = new AuthenticationDTO("username", "password");
        User existingUser = User.builder()
                .username("existingUser")
                .password("existingPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(existingUser));

        assertThrows(RegistrationException.class, () -> securityService.registration(request));

        verify(auditService, times(1)).audit(anyLong(), eq(AuditAction.REGISTRATION), eq(AuditType.FAIL));
    }

    @Test
    @DisplayName("Successful Authentication")
    public void testAuthentication_Successful() {
        AuthenticationDTO request = new AuthenticationDTO("username", "password");
        User existingUser = User.builder()
                .username("username")
                .password("password")
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(existingUser));

        User authenticatedUser = securityService.authenticate(request);

        assertNotNull(authenticatedUser);
        assertEquals("username", authenticatedUser.getUsername());
        assertEquals("password", authenticatedUser.getPassword());
        assertEquals(Role.USER, authenticatedUser.getRole());
    }

    @Test
    @DisplayName("Authentication: User Not Found")
    public void testAuthentication_UserNotFound() {
        AuthenticationDTO request = new AuthenticationDTO("username", "password");

        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> securityService.authenticate(request));

        verify(auditService, times(1)).audit(anyLong(), eq(AuditAction.LOG_IN), eq(AuditType.FAIL));
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
}
