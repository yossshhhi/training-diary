package kz.yossshhhi.controller;

import kz.yossshhhi.exception.InvalidCredentialsException;
import kz.yossshhhi.in.console.AuthenticationRequest;
import kz.yossshhhi.model.User;
import kz.yossshhhi.service.SecurityService;

/**
 * Controller class for handling security-related operations such as user registration and authentication.
 */
public class SecurityController {

    /** The security service responsible for handling registration and authentication logic. */
    private final SecurityService securityService;

    /**
     * Constructs a new SecurityController with the specified SecurityService.
     *
     * @param securityService the security service to be used for registration and authentication
     */
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    /**
     * Registers a new user with the provided username and password.
     *
     * @param username the username of the user to be registered
     * @param password the password of the user to be registered
     * @return the registered user
     * @throws InvalidCredentialsException if the username or password is invalid
     */
    public User register(String username, String password) {
        validUsername(username);
        validPassword(password);

        return securityService.registration(AuthenticationRequest.builder()
                .username(username)
                .password(password)
                .build());
    }

    /**
     * Authenticates a user with the provided username and password.
     *
     * @param username the username of the user to be authenticated
     * @param password the password of the user to be authenticated
     * @return the authenticated user
     * @throws InvalidCredentialsException if the username or password is invalid
     */
    public User authenticate(String username, String password) {
        validUsername(username);
        validPassword(password);

        return securityService.authenticate(AuthenticationRequest.builder()
                .username(username)
                .password(password)
                .build());
    }

    /**
     * Validates the username to ensure it meets the required criteria.
     *
     * @param username the username to be validated
     * @throws InvalidCredentialsException if the username is invalid
     */
    public void validUsername(String username) {
        if (username.isBlank()) {
            throw new InvalidCredentialsException("Username must not be blank");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new InvalidCredentialsException("Username length must be between 3 and 50");
        }
    }

    /**
     * Validates the password to ensure it meets the required criteria.
     *
     * @param password the password to be validated
     * @throws InvalidCredentialsException if the password is invalid
     */
    public void validPassword(String password) {
        if (password.isBlank()) {
            throw new InvalidCredentialsException("Password must not be blank");
        }
        if (password.length() < 5 || password.length() > 50) {
            throw new InvalidCredentialsException("Password length must be between 5 and 50");
        }
    }
}

