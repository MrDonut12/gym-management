package group4.gym.management.controller.plan;

import group4.gym.management.service.PlanTypeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create a plan type within the fixed catalog 1 day / 1 month / 6 months / 1 year (CN-09). Owner only.
 */
@WebServlet(name = "PlanTypeCreateServlet", urlPatterns = {"/plans/create"})
public class PlanTypeCreateServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/plan/form.jsp";

    private final PlanTypeService planTypeService = new PlanTypeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Set mode = create and the fixed list of allowed durations on the request.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read name, duration (value + unit), price -> PlanType; planTypeService.createPlanType.
        // Output: success: redirect to /plans; failure: forward to VIEW with errors.
    }
}
