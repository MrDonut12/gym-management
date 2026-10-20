package group4.gym.management.controller.payment;

import group4.gym.management.service.PaymentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cancel a PENDING transaction and its related registration (CN-21, QT-21). Manager, Receptionist. POST only.
 */
@WebServlet(name = "TransactionCancelServlet", urlPatterns = {"/transactions/cancel"})
public class TransactionCancelServlet extends HttpServlet {

    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id; paymentService.cancelPendingTransaction(currentAccount, id). PAID transactions cannot be cancelled.
        // Output: redirect to /transactions/detail?id=<id> with a success/error message.
    }
}
