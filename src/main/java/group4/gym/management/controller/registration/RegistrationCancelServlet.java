package group4.gym.management.controller.registration;

import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.exception.NotFoundException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.service.registration.RegistrationService;
import group4.gym.management.util.RequestUtil;
import group4.gym.management.util.SessionUtil;
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
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String reason = RequestUtil.getString(request, "reason");

        try {
            registrationService.cancel(SessionUtil.getSessionUser(request), id, reason);
            SessionUtil.flashMessage(request, "Đã hủy đăng ký");
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (ValidationException ex) {
            SessionUtil.flashError(request, ex.getFieldErrors().values().iterator().next());
        } catch (BusinessRuleException ex) {
            SessionUtil.flashError(request, ex.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/staff/registrations");
    }
}
