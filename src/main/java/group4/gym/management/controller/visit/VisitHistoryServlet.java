package group4.gym.management.controller.visit;

import group4.gym.management.service.VisitService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Look up visit history, filtered by member, date and status (CN-16). Owner, Manager, Receptionist.
 * Editing/cancelling visits belongs to FUT-01 and is not implemented.
 */
@WebServlet(name = "VisitHistoryServlet", urlPatterns = {"/visits"})
public class VisitHistoryServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/visit/history.jsp";

    private final VisitService visitService = new VisitService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read memberId, fromDate, toDate, status -> VisitFilter.
        //       visitService.searchHistory(filter) (includes the catch-up close step); set the list and the filter on the request.
        // Output: forward to VIEW.
    }
}
