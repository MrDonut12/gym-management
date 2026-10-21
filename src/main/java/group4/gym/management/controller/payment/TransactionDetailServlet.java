package group4.gym.management.controller.payment;

import group4.gym.management.service.PaymentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Transaction detail (CN-23). The amount/status of a completed transaction cannot be edited.
 */
@WebServlet(name = "TransactionDetailServlet", urlPatterns = {"/transactions/detail"})
public class TransactionDetailServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/transaction/detail.jsp";

    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id; paymentService.getTransactionDetail(id); set it on the request.
        // Output: forward to VIEW; 404 if it does not exist.
    }
}
