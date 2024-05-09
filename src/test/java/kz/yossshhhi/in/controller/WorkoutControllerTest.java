package kz.yossshhhi.in.controller;

import kz.yossshhhi.dto.AggregateWorkoutDataDTO;
import kz.yossshhhi.dto.ExtraOptionTypeDTO;
import kz.yossshhhi.dto.WorkoutDTO;
import kz.yossshhhi.dto.WorkoutTypeDTO;
import kz.yossshhhi.model.Workout;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Workout Controller tests")
class WorkoutControllerTest {
    private MockMvc mockMvc;

    @Mock
    private WorkoutService workoutService;

    @Mock
    private ExtraOptionTypeService extraOptionTypeService;

    @Mock
    private WorkoutTypeService workoutTypeService;

    @InjectMocks
    private WorkoutController workoutController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(workoutController).build();
    }

    @Test
    @DisplayName("GET /user/workouts - Show all user's workouts")
    void shouldShowAllUserWorkouts() throws Exception {
        List<WorkoutDTO> workouts = Arrays.asList(mock(WorkoutDTO.class), mock(WorkoutDTO.class));
        when(workoutService.findAllByUserId(anyLong())).thenReturn(workouts);

        mockMvc.perform(get("/user/workouts").requestAttr("user_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @DisplayName("POST /user/record - Recording a workout")
    void shouldRecordWorkout() throws Exception {
        Workout workout = mock(Workout.class);
        when(workoutService.create(any(WorkoutDTO.class), anyLong())).thenReturn(workout);

        mockMvc.perform(post("/user/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Test Workout\"}")
                        .requestAttr("user_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Training successfully recorded"));
    }

    @Test
    @DisplayName("DELETE /user/workouts/delete - Deleting a workout by id")
    void shouldDeleteWorkout() throws Exception {
        when(workoutService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/user/workouts/delete")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Workout successfully deleted"));
    }

    @Test
    @DisplayName("GET /user/statistic - Gives statistics on training for the desired period")
    void shouldGetStatistics() throws Exception {
        AggregateWorkoutDataDTO stats = new AggregateWorkoutDataDTO(10L, null, null);
        when(workoutService.getStatistics(anyLong(), anyInt())).thenReturn(stats);

        mockMvc.perform(get("/user/statistic")
                        .param("days", "30")
                        .requestAttr("user_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workoutCount").value(10));
    }

    @Test
    @DisplayName("GET /user/extra-option-types - Show all extra option types")
    void shouldShowAllExtraOptionTypes() throws Exception {
        List<ExtraOptionTypeDTO> extraOptionTypes = Arrays.asList(mock(ExtraOptionTypeDTO.class), mock(ExtraOptionTypeDTO.class));
        when(extraOptionTypeService.findAll()).thenReturn(extraOptionTypes);

        mockMvc.perform(get("/user/extra-option-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @DisplayName("GET /user/workout-types - Show all workout types")
    void shouldShowWorkoutTypes() throws Exception {
        List<WorkoutTypeDTO> workoutTypes = Arrays.asList(mock(WorkoutTypeDTO.class), mock(WorkoutTypeDTO.class));
        when(workoutTypeService.findAll()).thenReturn(workoutTypes);

        mockMvc.perform(get("/user/workout-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

}