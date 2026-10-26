package group4.gym.management.controller.registration;

import group4.gym.management.service.registration.PackageService;
import group4.gym.management.service.registration.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create a PENDING registration (CN-22): choose a member not soft-deleted and a package on sale. MANAGER, RECEPTIONIST.
 * The price is taken from the database, never from the form.
 */
@WebServlet(name = "RegistrationCreateServlet", urlPatterns = {"/staff/registrations/create"})
public class RegistrationCreateServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/registration/create.jsp";

    private final RegistrationService registrationService = new RegistrationService();
    private final PackageService packageService = new PackageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Optional memberId (from the CN-13 button) pre-selects the member; optional keyword ->
        //       registrationService.findSelectableMembers(keyword); packages = packageService.listOnSale().
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read memberId, packageId only (ignore any price/date parameter);
        //       regId = registrationService.createPending(sessionUser, memberId, packageId).
        //       BusinessRuleException (package not on sale / member soft-deleted) -> forward to VIEW with the error.
        // Output: success: redirect to /staff/payments/confirm?id=<regId> (CN-25 review step); failure: forward to VIEW.
    }
}
