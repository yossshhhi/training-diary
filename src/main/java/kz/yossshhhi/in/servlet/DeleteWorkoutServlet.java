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
import kz.yossshhhi.service.WorkoutService;

import java.io.IOException;

@WebServlet("/user/workout/delete")
public class DeleteWorkoutServlet extends HttpServlet {
    private WorkoutService workoutService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        workoutService = (WorkoutService) getServletContext().getAttribute("workoutService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        try {
            WorkoutDTO workoutDTO = objectMapper.readValue(req.getReader(), WorkoutDTO.class);
            if (workoutDTO.id() != null) {
                workoutService.delete(workoutDTO.id());
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getWriter(), new SuccessResponse("Workout successfully deleted"));
            } else {
                throw new IllegalArgumentException();
            }
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }
}
