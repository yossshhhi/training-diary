package kz.yossshhhi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import kz.yossshhhi.model.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility class for handling JWT for authentication.
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration-time}")
    private long expirationTime;

    /**
     * Generates a JWT for a user with specific username, role, and ID.
     * @param username the user's username
     * @param role the user's role
     * @param id the user's ID
     * @return a signed JWT string
     */
    public String generateToken(String username, String role, String id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(username)
                .id(id)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * Extracts the username from the given JWT.
     *
     * @param token the JWT string
     * @return the username
     */
    public String getUsername(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    /**
     * Extracts the user ID from the given JWT.
     *
     * @param token the JWT string
     * @return the user ID
     */
    public String getUserId(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload().getId();
    }

    /**
     * Extracts the user role from the given JWT.
     *
     * @param token the JWT string
     * @return the user's role
     */
    public Role getUserRole(String token) {
        Claims claims = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
        String role = (String) claims.get("role");
        return Role.valueOf(role);
    }

    /**
     * Validates the provided JWT string.
     *
     * @param token the JWT string to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extracts the JWT from the HTTP request's Authorization header.
     *
     * @param request the incoming HTTP request
     * @return the JWT string or null if not found or improperly formatted
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Generates a SecretKey using the configured secret key string.
     *
     * @return the secret key
     */
    private SecretKey getSecretKey() {
        byte[] decodedKey = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}
