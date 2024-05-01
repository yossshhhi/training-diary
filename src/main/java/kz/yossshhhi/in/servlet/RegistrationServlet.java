package kz.yossshhhi.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.yossshhhi.dto.AuthenticationDTO;
import kz.yossshhhi.dto.ExceptionResponse;
import kz.yossshhhi.dto.SuccessResponse;
import kz.yossshhhi.exception.InvalidCredentialsException;
import kz.yossshhhi.exception.RegistrationException;
import kz.yossshhhi.model.User;
import kz.yossshhhi.security.JwtTokenProvider;
import kz.yossshhhi.service.SecurityService;

import java.io.IOException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    private SecurityService securityService;
    private JwtTokenProvider tokenProvider;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        securityService = (SecurityService) getServletContext().getAttribute("securityService");
        tokenProvider = (JwtTokenProvider) getServletContext().getAttribute("jwtTokenProvider");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        try {
            AuthenticationDTO authenticationDTO = objectMapper.readValue(req.getReader(), AuthenticationDTO.class);
            User user = securityService.registration(authenticationDTO);
            String token = tokenProvider.generateToken(user.getUsername(), user.getRole().toString(), String.valueOf(user.getId()));

            resp.setHeader("Authorization", "Bearer " + token);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), new SuccessResponse("User " + user.getUsername() + " successfully registered"));
        } catch (RegistrationException | InvalidCredentialsException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Registration failed: " + e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }
}

