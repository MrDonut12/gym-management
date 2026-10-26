package group4.gym.management.controller.payment;

import group4.gym.management.service.payment.ReportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Member overview (CN-28), the MANAGER home page. MANAGER only.
 */
@WebServlet(name = "MemberOverviewServlet", urlPatterns = {"/manager/overview"})
public class MemberOverviewServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/payment/overview.jsp";

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: range = DateRange.of(fromDate, toDate) (for "new members"; default current month);
        //       overview = reportService.memberOverview(range); set overview and range on the request.
        // Output: forward to VIEW.
    }
}
