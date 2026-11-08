package group4.gym.management.controller.payment;

import group4.gym.management.model.payment.RevenueRow;
import group4.gym.management.service.payment.ReportService;
import group4.gym.management.util.DateRange;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Revenue per package: current package name, PAID count, revenue, share %. Uses the same range rules as the
 * period revenue report. MANAGER only.
 */
@WebServlet(name = "PackageRevenueReportServlet", urlPatterns = {"/manager/reports/revenue-by-package"})
public class PackageRevenueReportServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/payment/package-revenue.jsp";

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DateRange range = ReportRange.read(request);
        List<RevenueRow> rows = reportService.revenueByPackage(range);
        request.setAttribute("rows", rows);
        request.setAttribute("total", reportService.totalOf(rows));
        request.getRequestDispatcher(VIEW).forward(request, response);
    }
}
