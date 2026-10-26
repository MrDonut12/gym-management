package group4.gym.management.controller.payment;

import group4.gym.management.service.payment.PaymentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Two-step payment confirmation with plain JSP (CN-25). MANAGER, RECEPTIONIST.
 * GET = step 1 (review, changes nothing; going back changes nothing). POST = step 2 (final confirmation, QT-06).
 * Amount and dates are never read from the request (KT-20).
 */
@WebServlet(name = "PaymentConfirmServlet", urlPatterns = {"/staff/payments/confirm"})
public class PaymentConfirmServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/payment/confirm.jsp";

    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: id = getPositiveId (null / NotFoundException -> 404).
        //       PENDING -> paymentService.previewSchedule(id): set preview (member, package, amount snapshot, expected dates).
        //       PAID (after the redirect of a successful POST) -> show the saved schedule (start_date, end_date, paid_at, method).
        //       Otherwise (CANCELLED, member soft-deleted) -> show the error without the form.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id and paymentMethod (getEnum PaymentMethod) ONLY; ignore amount/date parameters.
        //       paymentService.confirm(id, method, sessionUser.getId());
        //       ValidationException (no method, HL-14) or BusinessRuleException (no longer PENDING / member soft-deleted)
        //       -> flash error, data unchanged.
        // Output: redirect to /staff/payments/confirm?id=<id> (PRG, CL-07), which then shows the saved schedule or the error.
    }
}
