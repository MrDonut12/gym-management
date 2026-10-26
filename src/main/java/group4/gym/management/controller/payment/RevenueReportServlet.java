package group4.gym.management.controller.payment;

import group4.gym.management.service.payment.ReportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Revenue per period (CN-26): date range + granularity (day/month); period, PAID count, revenue and a total row.
 * MANAGER only.
 */
@WebServlet(name = "RevenueReportServlet", urlPatterns = {"/manager/reports/revenue"})
public class RevenueReportServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/payment/revenue.jsp";

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: range = DateRange.of(fromDate, toDate) (default current month; invalid -> error, HL-16); granularity (default DAY);
        //       rows = reportService.revenueByPeriod(range, granularity); total = reportService.totalOf(rows).
        // Output: forward to VIEW.
    }
}
