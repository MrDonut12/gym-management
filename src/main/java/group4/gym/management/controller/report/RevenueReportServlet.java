package group4.gym.management.controller.report;

import group4.gym.management.service.ReportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Daily/monthly revenue report (CN-26) and plan revenue (CN-27). Owner, Manager, Accountant.
 */
@WebServlet(name = "RevenueReportServlet", urlPatterns = {"/reports/revenue"})
public class RevenueReportServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/report/revenue.jsp";

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read fromDate, toDate, granularity (DAY/MONTH); default to the current month.
        //       reportService.getRevenue, getTotalRevenue, getPlanRevenue; set the results on the request.
        // Output: forward to VIEW.
    }
}
