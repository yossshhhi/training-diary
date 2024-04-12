package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.UserRepository;
import kz.yossshhhi.exception.AuthenticationException;
import kz.yossshhhi.exception.RegistrationException;
import kz.yossshhhi.in.console.AuthenticationRequest;
import kz.yossshhhi.model.enums.AuditAction;
import kz.yossshhhi.model.enums.AuditType;
import kz.yossshhhi.model.enums.Role;
import kz.yossshhhi.model.User;

import java.util.Optional;

/**
 * Provides security-related services such as user registration and authentication.
 */
public class SecurityService {
    /**
     * The repository for managing user data.
     */
    private final UserRepository userRepository;

    /**
     * The service responsible for auditing user actions.
     */
    private final AuditService auditService;

    /**
     * Constructs a new SecurityService with the specified UserRepository and AuditService.
     *
     * @param userRepository The repository for managing user data.
     * @param auditService   The service responsible for auditing user actions.
     */
    public SecurityService(UserRepository userRepository, AuditService auditService) {
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    /**
     * Registers a new user based on the provided authentication request.
     *
     * @param request The authentication request containing user details.
     * @return The newly registered user.
     * @throws RegistrationException If a user with the provided username already exists.
     */
    public User registration(AuthenticationRequest request) {
        userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
            auditService.audit(-1L, AuditAction.REGISTRATION, AuditType.FAIL);
            throw new RegistrationException("User with " + user.getUsername() + " username already exists");
        });

        User user = userRepository.save(User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(Role.USER)
                .build());
        auditService.audit(user.getId(), AuditAction.REGISTRATION, AuditType.SUCCESS);
        return user;
    }

    /**
     * Authenticates a user based on the provided authentication request.
     *
     * @param request The authentication request containing user credentials.
     * @return The authenticated user.
     * @throws AuthenticationException If the user is not found or the password is invalid.
     */
    public User authenticate(AuthenticationRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isEmpty()) {
            auditService.audit(-1L, AuditAction.LOG_IN, AuditType.FAIL);
            throw new AuthenticationException("User not found");
        }

        User user = userOptional.get();

        if (!user.getPassword().equals(request.getPassword())) {
            auditService.audit(user.getId(), AuditAction.LOG_IN, AuditType.FAIL);
            throw new AuthenticationException("Invalid password");
        }

        auditService.audit(user.getId(), AuditAction.LOG_IN, AuditType.SUCCESS);
        return user;
    }
}

