package kz.yossshhhi.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.yossshhhi.dto.SuccessResponse;
import kz.yossshhhi.dto.WorkoutDTO;
import kz.yossshhhi.mapper.WorkoutMapper;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.service.WorkoutService;

import java.io.IOException;
import java.util.List;

@WebServlet("/user/workouts")
public class ShowUserWorkoutsServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        long userId = Long.parseLong((String) req.getAttribute("user_id"));

        List<Workout> workouts = workoutService.findAllByUserId(userId);
        List<WorkoutDTO> workoutDTOS = workouts.stream().map(workoutMapper::toDTO).toList();
        resp.setStatus(HttpServletResponse.SC_OK);
        if (!workoutDTOS.isEmpty()) {
            objectMapper.writeValue(resp.getWriter(), workoutDTOS);
        } else {
            objectMapper.writeValue(resp.getWriter(), new SuccessResponse("There are no training recordings yet"));
        }
    }
}
