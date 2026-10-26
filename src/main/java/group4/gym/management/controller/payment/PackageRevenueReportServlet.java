package group4.gym.management.controller.payment;

import group4.gym.management.service.payment.ReportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Revenue per package (CN-27): current package name, PAID count, revenue, share %. Same range rules as CN-26.
 * MANAGER only.
 */
@WebServlet(name = "PackageRevenueReportServlet", urlPatterns = {"/manager/reports/revenue-by-package"})
public class PackageRevenueReportServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/payment/package-revenue.jsp";

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: range = DateRange.of(fromDate, toDate); rows = reportService.revenueByPackage(range); total = reportService.totalOf(rows).
        // Output: forward to VIEW.
    }
}
