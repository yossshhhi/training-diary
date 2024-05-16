package kz.yossshhhi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.yossshhhi.dto.ExceptionResponse;
import kz.yossshhhi.model.enums.Role;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter that intercepts requests to authenticate and authorize using JWT tokens.
 * This filter checks for the presence of a JWT token, validates it, and extracts user details,
 * applying security constraints based on the user's role and requested endpoint.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    /**
     * Processes an incoming request to authenticate and authorize the user based on a JWT token.
     * Validates the token, extracts user details, and checks access rights based on user roles and endpoint.
     *
     * @param request The request object.
     * @param response The response object.
     * @param filterChain The filter chain for passing the request further down the filter chain.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException If an I/O error occurs during request processing.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
        } else {
            String token = tokenProvider.resolveToken(request);
            if (token != null && tokenProvider.validateToken(token)) {
                Role role = tokenProvider.getUserRole(token);
                String username = tokenProvider.getUsername(token);
                String userId = tokenProvider.getUserId(token);
                String path = request.getRequestURI();
                request.setAttribute("username", username);
                request.setAttribute("user_id", userId);

                if (isAdminEndpoint(path) && role.name().equals("USER")) {
                    unauthorizedAccess(response, "Access denied. Insufficient privileges.");
                }
                filterChain.doFilter(request, response);
            } else {
                unauthorizedAccess(response, "Unauthorized access. Please provide a valid token.");
            }
        }
    }

    /**
     * Determines if the requested path is an admin endpoint.
     *
     * @param path The request URI to check.
     * @return true if the path is for an admin endpoint, false otherwise.
     */
    private boolean isAdminEndpoint(String path) {
        return path.startsWith("/admin/");
    }

    /**
     * Constructs and sends an unauthorized access response with a custom message.
     *
     * @param response The HttpServletResponse to which the unauthorized message is written.
     * @param message The error message describing the nature of the unauthorized access.
     * @throws IOException If an error occurs while writing to the response.
     */
    private void unauthorizedAccess(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), new ExceptionResponse(message));
    }
}

