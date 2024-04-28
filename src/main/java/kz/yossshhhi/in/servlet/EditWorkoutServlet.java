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
import kz.yossshhhi.model.ExtraOption;
import kz.yossshhhi.model.Workout;
import kz.yossshhhi.service.ExtraOptionService;
import kz.yossshhhi.service.WorkoutService;

import java.io.IOException;
import java.util.List;

@WebServlet("/user/workout/edit")
public class EditWorkoutServlet extends HttpServlet {
    private WorkoutService workoutService;
    private ExtraOptionService extraOptionService;
    private WorkoutMapper workoutMapper;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        workoutService = (WorkoutService) getServletContext().getAttribute("workoutService");
        extraOptionService = (ExtraOptionService) getServletContext().getAttribute("extraOptionService");
        workoutMapper = (WorkoutMapper) getServletContext().getAttribute("workoutMapper");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        try {
            WorkoutDTO workoutDTO = objectMapper.readValue(req.getReader(), WorkoutDTO.class);
            Workout workout = workoutService.findById(workoutDTO.id());
            workoutService.update(map(workoutDTO, workout));
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), new SuccessResponse("Workout successfully updated"));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }

    private Workout map(WorkoutDTO workoutDTO, Workout workout) {
        if (workoutDTO.duration() != null) {
            workout.setDuration(workoutDTO.duration());
        }
        if (workoutDTO.burnedCalories() != null) {
            workout.setBurnedCalories(workoutDTO.burnedCalories());
        }
        if (workoutDTO.workoutTypeId() != null) {
            workout.setWorkoutTypeId(workoutDTO.workoutTypeId());
        }
        if (workoutDTO.extraOptions() != null) {
            extraOptionService.deleteAll(workout.getExtraOptions());
            List<ExtraOption> options = workoutDTO.extraOptions().stream().map(workoutMapper::toExtraOption).toList();
            workout.setExtraOptions(options);
        }
        return workout;
    }
}
