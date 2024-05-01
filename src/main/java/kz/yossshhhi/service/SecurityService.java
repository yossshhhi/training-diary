package kz.yossshhhi.service;

import kz.yossshhhi.aop.Auditable;
import kz.yossshhhi.aop.Loggable;
import kz.yossshhhi.dao.repository.UserRepository;
import kz.yossshhhi.dto.AuthenticationDTO;
import kz.yossshhhi.exception.AuthenticationException;
import kz.yossshhhi.exception.InvalidCredentialsException;
import kz.yossshhhi.exception.RegistrationException;
import kz.yossshhhi.model.User;
import kz.yossshhhi.model.enums.AuditAction;
import kz.yossshhhi.model.enums.Role;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

/**
 * Provides security-related services such as user registration and authentication.
 */
@Loggable
public class SecurityService {
    /**
     * The repository for managing user data.
     */
    private final UserRepository userRepository;

    /**
     * Constructs a new SecurityService with the specified UserRepository and AuditService.
     *
     * @param userRepository The repository for managing user data.
     */
    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user based on the provided authentication request.
     *
     * @param request The authentication request containing user details.
     * @return The newly registered user.
     * @throws RegistrationException If a user with the provided username already exists.
     */
    @Auditable(action = AuditAction.REGISTRATION)
    public User registration(AuthenticationDTO request) {
        validRequest(request);

        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new RegistrationException("User with " + user.getUsername() + " username already exists");
        });

        String hashedPassword = hashPassword(request.password());

        return userRepository.save(User.builder()
                .username(request.username())
                .password(hashedPassword)
                .role(Role.USER)
                .build());
    }

    /**
     * Authenticates a user based on the provided authentication request.
     *
     * @param request The authentication request containing user credentials.
     * @return The authenticated user.
     * @throws AuthenticationException If the user is not found or the password is invalid.
     */
    @Auditable(action = AuditAction.LOG_IN)
    public User authenticate(AuthenticationDTO request) {
        validRequest(request);

        Optional<User> userOptional = userRepository.findByUsername(request.username());
        if (userOptional.isEmpty()) {
            throw new AuthenticationException("User not found");
        }

        User user = userOptional.get();
        String hashedPassword = hashPassword(request.password());
        if (!user.getPassword().equals(hashedPassword)) {
            throw new AuthenticationException("Invalid password");
        }

        return user;
    }

    private void validRequest(AuthenticationDTO request) {
        if (request.username().isBlank() || request.password().isBlank()) {
            throw new InvalidCredentialsException("Username or password is blank");
        }

        if (request.username().length() < 3 || request.username().length() > 50) {
            throw new InvalidCredentialsException("Username length must be between 3 and 50");
        }

        if (!request.username().matches("^[a-zA-Z0-9]+$")) {
            throw new InvalidCredentialsException("Username must consist of letters and numbers only");
        }

        if (request.password().length() < 5 || request.password().length() > 50) {
            throw new InvalidCredentialsException("Password length must be between 5 and 50");
        }
    }

    /**
     * Hashes the provided password using SHA-256 algorithm.
     *
     * @param password The password to hash.
     * @return The hashed password.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}

