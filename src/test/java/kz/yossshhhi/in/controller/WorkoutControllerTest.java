package kz.yossshhhi.in.controller;

import kz.yossshhhi.dto.AggregateWorkoutDataDTO;
import kz.yossshhhi.dto.ExtraOptionTypeDTO;
import kz.yossshhhi.dto.WorkoutDTO;
import kz.yossshhhi.dto.WorkoutTypeDTO;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.security.JwtTokenFilter;
import kz.yossshhhi.service.ExtraOptionTypeService;
import kz.yossshhhi.service.WorkoutService;
import kz.yossshhhi.service.WorkoutTypeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = WorkoutController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtTokenFilter.class)})
@DisplayName("Workout Controller tests")
class WorkoutControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkoutService workoutService;

    @MockBean
    private ExtraOptionTypeService extraOptionTypeService;

    @MockBean
    private WorkoutTypeService workoutTypeService;

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