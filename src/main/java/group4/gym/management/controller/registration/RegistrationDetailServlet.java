package group4.gym.management.controller.registration;

import group4.gym.management.exception.NotFoundException;
import group4.gym.management.service.registration.RegistrationService;
import group4.gym.management.util.RequestUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Registration detail (FR-24): every column of one registration, any status. Read-only. MANAGER, RECEPTIONIST.
 */
@WebServlet(name = "RegistrationDetailServlet", urlPatterns = {"/staff/registrations/detail"})
public class RegistrationDetailServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/registration/detail.jsp";

    private final RegistrationService registrationService = new RegistrationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            request.setAttribute("view", registrationService.getView(id));
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.getRequestDispatcher(VIEW).forward(request, response);
    }
}
