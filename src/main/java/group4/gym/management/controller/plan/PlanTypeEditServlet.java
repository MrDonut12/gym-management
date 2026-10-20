package group4.gym.management.controller.plan;

import group4.gym.management.service.PlanTypeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Edit plan type name and price (CN-09). Owner, Manager. The duration cannot be edited.
 */
@WebServlet(name = "PlanTypeEditServlet", urlPatterns = {"/plans/edit"})
public class PlanTypeEditServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/plan/form.jsp";

    private final PlanTypeService planTypeService = new PlanTypeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id; planTypeService.getById; set planType, mode = edit (duration is display-only, not editable).
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id, name, price (ignore any duration parameter); planTypeService.updateNameAndPrice.
        // Output: success: redirect to /plans; failure: forward to VIEW with errors.
    }
}
