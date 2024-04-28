package kz.yossshhhi.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.yossshhhi.dto.ExceptionResponse;
import kz.yossshhhi.dto.SuccessResponse;
import kz.yossshhhi.dto.WorkoutDTO;
import kz.yossshhhi.mapper.WorkoutMapper;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.service.WorkoutService;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/user/record")
public class RecordWorkoutServlet extends HttpServlet {
    private WorkoutService workoutService;
    private WorkoutMapper workoutMapper;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        workoutService = (WorkoutService) getServletContext().getAttribute("workoutService");
        workoutMapper = (WorkoutMapper) getServletContext().getAttribute("workoutMapper");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        long userId = Long.parseLong((String) req.getAttribute("user_id"));
        try {
            WorkoutDTO workoutDTO = objectMapper.readValue(req.getReader(), WorkoutDTO.class);
            workoutService.create(getWorkout(workoutDTO, userId));
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), new SuccessResponse("Training successfully recorded"));
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Invalid request: " + e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Internal server error: " + e.getMessage()));
        }
    }

    private Workout getWorkout(WorkoutDTO workoutDTO, long userId) {
        Workout entity = workoutMapper.toEntity(workoutDTO);
        entity.setUserId(userId);
        entity.setCreatedAt(LocalDate.now());
        return entity;
    }
}
