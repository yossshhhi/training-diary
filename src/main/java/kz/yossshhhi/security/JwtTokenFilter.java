package kz.yossshhhi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.yossshhhi.dto.ExceptionResponse;
import kz.yossshhhi.model.enums.Role;

import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*", "/user/*"})
public class JwtTokenFilter implements Filter {
    private JwtTokenProvider tokenProvider;
    private ObjectMapper objectMapper;

    @Override
    public void init(FilterConfig filterConfig) {
        ServletContext servletContext = filterConfig.getServletContext();
        tokenProvider = (JwtTokenProvider) servletContext.getAttribute("jwtTokenProvider");
        objectMapper = (ObjectMapper) servletContext.getAttribute("objectMapper");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = tokenProvider.resolveToken(request);
        if (token != null && tokenProvider.validateToken(token)) {
            String username = tokenProvider.getUsername(token);
            Role role = tokenProvider.getUserRole(token);
            String userId = tokenProvider.getUserId(token);
            request.setAttribute("username", username);
            request.setAttribute("user_id", userId);
            request.setAttribute("role", role);
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(response.getWriter(), new ExceptionResponse("Unauthorized access. Please provide a valid token."));
        }
    }
}

