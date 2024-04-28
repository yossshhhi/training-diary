package kz.yossshhhi.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.yossshhhi.dto.WorkoutTypeDTO;
import kz.yossshhhi.mapper.WorkoutTypeMapper;
import kz.yossshhhi.model.WorkoutType;
import kz.yossshhhi.service.WorkoutTypeService;

import java.io.IOException;
import java.util.List;

@WebServlet("/user/workout-types")
public class ShowWorkoutTypesServlet extends HttpServlet {
    private WorkoutTypeService workoutTypeService;
    private WorkoutTypeMapper workoutTypeMapper;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        workoutTypeService = (WorkoutTypeService) getServletContext().getAttribute("workoutTypeService");
        workoutTypeMapper = (WorkoutTypeMapper) getServletContext().getAttribute("workoutTypeMapper");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        List<WorkoutType> types = workoutTypeService.findAll();
        List<WorkoutTypeDTO> typeDTOS = types.stream().map(workoutTypeMapper::toDTO).toList();
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), typeDTOS);
    }
}
