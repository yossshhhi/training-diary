package kz.yossshhhi.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.yossshhhi.dto.ExtraOptionTypeDTO;
import kz.yossshhhi.mapper.ExtraOptionTypeMapper;
import kz.yossshhhi.model.ExtraOptionType;
import kz.yossshhhi.service.ExtraOptionTypeService;

import java.io.IOException;
import java.util.List;

@WebServlet("/user/extra-option-types")
public class ShowExtraOptionTypesServlet extends HttpServlet {
    private ExtraOptionTypeService extraOptionTypeService;
    private ExtraOptionTypeMapper extraOptionTypeMapper;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        extraOptionTypeService = (ExtraOptionTypeService) getServletContext().getAttribute("extraOptionTypeService");
        extraOptionTypeMapper = (ExtraOptionTypeMapper) getServletContext().getAttribute("extraOptionTypeMapper");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        List<ExtraOptionType> types = extraOptionTypeService.findAll();
        List<ExtraOptionTypeDTO> typeDTOS = types.stream().map(extraOptionTypeMapper::toDTO).toList();
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), typeDTOS);
    }
}
