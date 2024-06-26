package kz.yossshhhi.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.yossshhhi.dto.AuthenticationDTO;
import kz.yossshhhi.model.User;
import kz.yossshhhi.model.enums.Role;
import kz.yossshhhi.security.JwtTokenFilter;
import kz.yossshhhi.security.JwtTokenProvider;
import kz.yossshhhi.service.SecurityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = SecurityController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtTokenFilter.class)})
@DisplayName("Security Controller tests")
class SecurityControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SecurityService securityService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("POST /register - Success")
    void whenRegisterUser_thenReturnsToken() throws Exception {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("user", "password");
        User user = new User(1L, "user", "password", Role.USER);
        when(securityService.registration(any(AuthenticationDTO.class))).thenReturn(user);
        when(jwtTokenProvider.generateToken(anyString(), anyString(), anyString())).thenReturn("token");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authenticationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.message").value("User user successfully registered"));
    }

    @Test
    @DisplayName("POST /login - Success")
    void whenLoginUser_thenReturnsToken() throws Exception {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("user", "password");
        User user = new User(1L, "user", "password", Role.USER);
        when(securityService.authenticate(any(AuthenticationDTO.class))).thenReturn(user);
        when(jwtTokenProvider.generateToken(anyString(), anyString(), anyString())).thenReturn("token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authenticationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.message").value("User user successfully logged in"));
    }
}