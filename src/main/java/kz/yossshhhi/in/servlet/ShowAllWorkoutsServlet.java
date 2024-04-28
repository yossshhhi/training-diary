package kz.yossshhhi.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kz.yossshhhi.dto.ExceptionResponse;
import kz.yossshhhi.dto.SuccessResponse;
import kz.yossshhhi.dto.WorkoutDTO;
import kz.yossshhhi.mapper.WorkoutMapper;
import kz.yossshhhi.model.User;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.model.enums.Role;
import kz.yossshhhi.service.WorkoutService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/workouts")
public class ShowAllWorkoutsServlet extends HttpServlet {

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

        Role role = (Role) req.getAttribute("role");

        if (role == Role.ADMIN) {
            List<Workout> workouts = workoutService.findAll();
            List<WorkoutDTO> dtoList = workouts.stream().map(workoutMapper::toDTO).toList();

            resp.setStatus(HttpServletResponse.SC_OK);
            if (dtoList.isEmpty()) {
                objectMapper.writeValue(resp.getWriter(), new SuccessResponse("There are no training recordings yet"));
            } else {
                objectMapper.writeValue(resp.getWriter(), dtoList);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Access denied. User does not have permission."));
        }
    }
}
