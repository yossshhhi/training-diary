package kz.yossshhhi.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.yossshhhi.dto.AuthenticationDTO;
import kz.yossshhhi.dto.TokenResponse;
import kz.yossshhhi.model.User;
import kz.yossshhhi.security.JwtTokenProvider;
import kz.yossshhhi.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling security operations for user authentication and registration.
 * Provides endpoints for registering new users and logging in existing users.
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Security Operations", description = "Handles user authentication and registration")
public class SecurityController {
    private final SecurityService securityService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Registers a new user with the provided user credentials.
     *
     * @param authenticationDTO The authentication data transfer object containing user credentials.
     * @return A {@link ResponseEntity} containing a {@link TokenResponse} with a JWT token
     *         and a message indicating successful registration. Returns HTTP Status 201 (Created).
     */
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a new user")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody AuthenticationDTO authenticationDTO) {
        User user = securityService.registration(authenticationDTO);

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().toString(), String.valueOf(user.getId()));

        TokenResponse response = new TokenResponse(token, "User " + user.getUsername() + " successfully registered");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticates a user and issues a JWT token.
     *
     * @param authenticationDTO The authentication data transfer object containing the login credentials.
     * @return A {@link ResponseEntity} containing a {@link TokenResponse} with a JWT token
     *         and a message indicating successful login. Returns HTTP Status 200 (OK).
     */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Authenticate user and provide token")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthenticationDTO authenticationDTO) {
        User user = securityService.authenticate(authenticationDTO);

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().toString(), String.valueOf(user.getId()));

        TokenResponse response = new TokenResponse(token, "User " + user.getUsername() + " successfully logged in");

        return ResponseEntity.ok(response);
    }
}
