package group4.gym.management.controller.payment;

import group4.gym.management.service.PaymentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Confirm payment as PAID (CN-21, CN-22, QT-26). Manager, Receptionist.
 * The confirmation screen has NO start-date input (FUT-03); a bank transfer proof image is optional (GT-03).
 */
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
@WebServlet(name = "PaymentConfirmServlet", urlPatterns = {"/transactions/confirm"})
public class PaymentConfirmServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/transaction/confirm.jsp";

    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id; paymentService.getTransactionDetail(id); only a PENDING transaction shows the confirmation form.
        //       paymentService.shouldWarnLateDailyPass(id) -> set the late Daily Pass (after 20:00) warning flag (does not block).
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id, paymentMethod, proof image (Part, optional); ignore every date parameter sent by the client.
        //       paymentService.confirmPayment(currentAccount, id, paymentMethod, proofImagePath);
        //       BusinessRuleException (no longer PENDING / member soft-deleted / save error) -> show the error, data unchanged.
        // Output: success: redirect to /transactions/detail?id=<id>; failure: forward to VIEW with an error.
    }
}
