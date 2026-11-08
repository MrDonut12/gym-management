package group4.gym.management.controller.payment;

import group4.gym.management.model.payment.RevenueGranularity;
import group4.gym.management.model.payment.RevenueRow;
import group4.gym.management.service.payment.ReportService;
import group4.gym.management.util.DateRange;
import group4.gym.management.util.RequestUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Revenue per period (FR-26): date range + granularity (day/month); period, PAID count, revenue and a total row.
 * MANAGER only.
 */
@WebServlet(name = "RevenueReportServlet", urlPatterns = {"/manager/reports/revenue"})
public class RevenueReportServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/payment/revenue.jsp";

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DateRange range = ReportRange.read(request);
        RevenueGranularity granularity = RequestUtil.getEnum(request, "granularity", RevenueGranularity.class);
        if (granularity == null) {
            granularity = RevenueGranularity.DAY;
        }

        List<RevenueRow> rows = reportService.revenueByPeriod(range, granularity);
        request.setAttribute("granularity", granularity);
        request.setAttribute("rows", rows);
        request.setAttribute("total", reportService.totalOf(rows));
        request.getRequestDispatcher(VIEW).forward(request, response);
    }
}
