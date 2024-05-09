package kz.yossshhhi.util;

import kz.yossshhhi.dto.ExceptionResponse;
import kz.yossshhhi.exception.AuthenticationException;
import kz.yossshhhi.exception.RegistrationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Centralizes exception handling across all @RequestMapping methods through @ExceptionHandler methods.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors thrown when request data does not meet validation constraints.
     *
     * @param ex The exception captured from failed validation.
     * @return A ResponseEntity containing the list of validation error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles exceptions specifically related to database operations.
     *
     * @param ex The DataAccessException instance thrown by DAO operations.
     * @return A ResponseEntity with a custom error message.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ExceptionResponse> handleDatabaseExceptions(DataAccessException ex) {
        return ResponseEntity.internalServerError().body(new ExceptionResponse("Database error: " + ex.getMessage()));
    }

    /**
     * Handles exceptions that arise during the registration or authentication process.
     *
     * @param ex The caught RegistrationException, AuthenticationException, or IllegalArgumentException.
     * @return A ResponseEntity detailing the bad request.
     */
    @ExceptionHandler({RegistrationException.class, AuthenticationException.class, IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse("Bad Request: " + ex.getMessage()));
    }

    /**
     * Handles HTTP method and missing parameter exceptions to provide a uniform bad request response.
     *
     * @param ex The exception indicating the HTTP request was malformed.
     * @return A ResponseEntity with details about the bad request.
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequestExceptions(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse("Bad Request: " + ex.getMessage()));
    }
}
