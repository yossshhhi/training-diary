package kz.yossshhhi.in.controller;

import kz.yossshhhi.dto.AuditDTO;
import kz.yossshhhi.dto.ExtraOptionTypeDTO;
import kz.yossshhhi.dto.WorkoutTypeDTO;
import kz.yossshhhi.model.ExtraOptionType;
import kz.yossshhhi.model.WorkoutType;
import kz.yossshhhi.service.AuditService;
import kz.yossshhhi.service.ExtraOptionTypeService;
import kz.yossshhhi.service.WorkoutService;
import kz.yossshhhi.service.WorkoutTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Admin Controller tests")
class AdminControllerTest {

    private MockMvc mockMvc;
    @Mock
    private WorkoutService workoutService;
    @Mock
    private AuditService auditService;
    @Mock
    private ExtraOptionTypeService extraOptionTypeService;
    @Mock
    private WorkoutTypeService workoutTypeService;
    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @DisplayName("GET /admin/workouts - Success")
    void shouldFetchAllWorkouts() throws Exception {
        mockMvc.perform(get("/admin/workouts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @DisplayName("GET /admin/audits - Success")
    void shouldFetchAllAudits() throws Exception {
        List<AuditDTO> auditList = Arrays.asList(mock(AuditDTO.class), mock(AuditDTO.class));
        when(auditService.findAll()).thenReturn(auditList);

        mockMvc.perform(get("/admin/audits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @DisplayName("POST /admin/add-extra-option-type - Success")
    void shouldCreateExtraOptionType() throws Exception {
        ExtraOptionType extraOptionType = mock(ExtraOptionType.class);
        when(extraOptionTypeService.create(any(ExtraOptionTypeDTO.class))).thenReturn(extraOptionType);

        mockMvc.perform(post("/admin/add-extra-option-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"newType\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Extra option type successfully created"));
    }

    @Test
    @DisplayName("POST /admin/add-workout-type - Success")
    void shouldCreateWorkoutType() throws Exception {
        String jsonContent = "{\"name\":\"newWorkoutType\"}";

        when(workoutTypeService.create(any(WorkoutTypeDTO.class))).thenReturn(new WorkoutType());

        mockMvc.perform(post("/admin/add-workout-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Workout type successfully created"));
    }
}