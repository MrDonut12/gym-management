package group4.gym.management.controller.report;

import group4.gym.management.service.ReportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Total member and new member statistics (CN-28). Owner, Manager.
 */
@WebServlet(name = "MemberReportServlet", urlPatterns = {"/reports/members"})
public class MemberReportServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/report/members.jsp";

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read fromDate, toDate and the profile status filter; reportService.getMemberStatistics.
        // Output: forward to VIEW.
    }
}
