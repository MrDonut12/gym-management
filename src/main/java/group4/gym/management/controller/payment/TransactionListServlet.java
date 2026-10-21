package group4.gym.management.controller.payment;

import group4.gym.management.service.PaymentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Look up and filter transactions by time and status (CN-23, CN-24). Owner, Manager, Receptionist, Accountant.
 */
@WebServlet(name = "TransactionListServlet", urlPatterns = {"/transactions"})
public class TransactionListServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/transaction/list.jsp";

    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read fromDate, toDate, statuses (multi-valued), memberId -> TransactionFilter.
        //       paymentService.searchTransactions(filter); set the list and the filter on the request.
        //       The UI states the filter key: PENDING/CANCELLED by created_at, PAID by paid_at.
        // Output: forward to VIEW.
    }
}
