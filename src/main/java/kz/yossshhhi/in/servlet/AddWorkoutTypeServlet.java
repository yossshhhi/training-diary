package kz.yossshhhi.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.yossshhhi.dto.ExceptionResponse;
import kz.yossshhhi.dto.ExtraOptionTypeDTO;
import kz.yossshhhi.dto.SuccessResponse;
import kz.yossshhhi.dto.WorkoutTypeDTO;
import kz.yossshhhi.mapper.ExtraOptionTypeMapper;
import kz.yossshhhi.mapper.WorkoutTypeMapper;
import kz.yossshhhi.model.enums.Role;
import kz.yossshhhi.service.ExtraOptionTypeService;
import kz.yossshhhi.service.WorkoutTypeService;

import java.io.IOException;

@WebServlet("/admin/add-workout-type")
public class AddWorkoutTypeServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Role role = (Role) req.getAttribute("role");

        try {
            if (role == Role.ADMIN) {
                WorkoutTypeDTO dto = objectMapper.readValue(req.getReader(), WorkoutTypeDTO.class);
                workoutTypeService.create(workoutTypeMapper.toEntity(dto));

                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getWriter(), new SuccessResponse("Workout type successfully created"));
            } else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Access denied. User does not have permission."));
            }
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Internal server error: " + e.getMessage()));
        }
    }
}
