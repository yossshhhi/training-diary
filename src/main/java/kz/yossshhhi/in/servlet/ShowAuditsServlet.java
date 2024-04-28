package kz.yossshhhi.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.yossshhhi.dto.AuditDTO;
import kz.yossshhhi.dto.ExceptionResponse;
import kz.yossshhhi.mapper.AuditMapper;
import kz.yossshhhi.model.Audit;
import kz.yossshhhi.model.enums.Role;
import kz.yossshhhi.service.AuditService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/audits")
public class ShowAuditsServlet extends HttpServlet {
    private AuditService auditService;
    private AuditMapper auditMapper;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        auditService = (AuditService) getServletContext().getAttribute("auditService");
        auditMapper = (AuditMapper) getServletContext().getAttribute("auditMapper");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Role role = (Role) req.getAttribute("role");

        if (role == Role.ADMIN) {
            List<Audit> audits = auditService.findAll();
            List<AuditDTO> dtoList = audits.stream().map(auditMapper::toDTO).toList();

            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), dtoList);
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Access denied. User does not have permission."));
        }
    }
}
