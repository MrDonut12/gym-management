package group4.gym.management.controller.report;

import group4.gym.management.service.ReportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * New registration and renewal/repurchase statistics (CN-29). Owner, Manager.
 */
@WebServlet(name = "PurchaseReportServlet", urlPatterns = {"/reports/purchases"})
public class PurchaseReportServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/report/purchases.jsp";

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read fromDate, toDate; reportService.getPurchaseStatistics.
        // Output: forward to VIEW.
    }
}
