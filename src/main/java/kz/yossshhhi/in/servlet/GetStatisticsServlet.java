package kz.yossshhhi.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.yossshhhi.dto.AggregateWorkoutDataDTO;
import kz.yossshhhi.dto.ExceptionResponse;
import kz.yossshhhi.mapper.AggregateWorkoutDataMapper;
import kz.yossshhhi.service.WorkoutService;

import java.io.IOException;

@WebServlet("/user/statistic")
public class GetStatisticsServlet extends HttpServlet {
    private WorkoutService workoutService;
    private AggregateWorkoutDataMapper mapper;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        workoutService = (WorkoutService) getServletContext().getAttribute("workoutService");
        mapper = (AggregateWorkoutDataMapper) getServletContext().getAttribute("aggregateWorkoutDataMapper");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        long userId = Long.parseLong((String) req.getAttribute("user_id"));
        try {
            Integer days = Integer.parseInt(req.getParameter("days"));
            AggregateWorkoutDataDTO dto = mapper.toDTO(workoutService.getStatistics(userId, days));
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), dto);
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }

    }
}
