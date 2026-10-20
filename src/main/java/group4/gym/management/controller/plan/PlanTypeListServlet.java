package group4.gym.management.controller.plan;

import group4.gym.management.service.PlanTypeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Plan type catalog (CN-09). Owner, Manager.
 */
@WebServlet(name = "PlanTypeListServlet", urlPatterns = {"/plans"})
public class PlanTypeListServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/plan/list.jsp";

    private final PlanTypeService planTypeService = new PlanTypeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: planTypeService.listAll(); set the list on the request.
        // Output: forward to VIEW.
    }
}
