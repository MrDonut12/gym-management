package group4.gym.management.controller.registration;

import group4.gym.management.service.registration.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cancel a PENDING registration with a reason (CN-23, QT-02, HL-15). MANAGER, RECEPTIONIST. POST only.
 * The reason is entered in the cancel form of the registration list (CN-24).
 */
@WebServlet(name = "RegistrationCancelServlet", urlPatterns = {"/staff/registrations/cancel"})
public class RegistrationCancelServlet extends HttpServlet {

    private final RegistrationService registrationService = new RegistrationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id (null / NotFoundException -> 404) and reason (getString);
        //       registrationService.cancel(sessionUser, id, reason).
        //       ValidationException (missing/short reason) or BusinessRuleException (no longer PENDING) -> flash error.
        // Output: redirect to /staff/registrations with a flash message.
    }
}
