package group4.gym.management.controller.payment;

import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.exception.NotFoundException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.account.SessionUser;
import group4.gym.management.model.registration.PaymentMethod;
import group4.gym.management.model.registration.RegistrationStatus;
import group4.gym.management.model.registration.RegistrationView;
import group4.gym.management.service.payment.PaymentService;
import group4.gym.management.util.RequestUtil;
import group4.gym.management.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Two-step payment confirmation with plain JSP (FR-25). MANAGER, RECEPTIONIST.
 * GET = step 1 (review, changes nothing; going back changes nothing). POST = step 2 (final confirmation, BR-06).
 * Amount and dates are never read from the request (AC-20).
 */
@WebServlet(name = "PaymentConfirmServlet", urlPatterns = {"/staff/payments/confirm"})
public class PaymentConfirmServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/payment/confirm.jsp";

    private final PaymentService paymentService = new PaymentService();

    /**
     * Request attributes for the view:
     * view (RegistrationView, always), preview (SchedulePreview, PENDING and confirmable),
     * blockedReason (PENDING but not confirmable, e.g. member soft-deleted, BR-11).
     * PAID shows the saved schedule; CANCELLED shows the cancellation, both without the form.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        RegistrationView view;
        try {
            view = paymentService.getView(id);
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("view", view);

        if (view.getRegistration().getStatus() == RegistrationStatus.PENDING) {
            try {
                request.setAttribute("preview", paymentService.previewSchedule(id));
            } catch (BusinessRuleException ex) {
                request.setAttribute("blockedReason", ex.getMessage());
            } catch (NotFoundException ex) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Only the ID and the payment method are read: any amount or date parameter is ignored (AC-20).
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        PaymentMethod method = RequestUtil.getEnum(request, "paymentMethod", PaymentMethod.class);
        SessionUser user = SessionUtil.getSessionUser(request);

        try {
            paymentService.confirm(id, method, user.getId());
            SessionUtil.flashMessage(request, "Payment confirmed. The package schedule below has been saved.");
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (ValidationException ex) {
            SessionUtil.flashError(request, ex.getMessage());
        } catch (BusinessRuleException ex) {
            SessionUtil.flashError(request, ex.getMessage());
        }
        // Post/Redirect/Get (NFR-07): the GET shows the saved schedule, or the review step again with the error.
        response.sendRedirect(request.getContextPath() + "/staff/payments/confirm?id=" + id);
    }
}
