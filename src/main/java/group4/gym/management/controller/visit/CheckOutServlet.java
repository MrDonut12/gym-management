package group4.gym.management.controller.visit;

import group4.gym.management.service.VisitService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Manual check-out (CN-15). Manager, Receptionist.
 */
@WebServlet(name = "CheckOutServlet", urlPatterns = {"/visits/check-out"})
public class CheckOutServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/visit/check-out.jsp";

    private final VisitService visitService = new VisitService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: visitService.listOpenVisits() (includes the catch-up close step); set the list on the request.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read visitId; visitService.checkOut(currentAccount, visitId).
        //       No OPEN visit, or 20:00 or later (already AUTO_CLOSED) -> reject with an appropriate message.
        // Output: redirect to /visits/check-out with a success/error message.
    }
}
