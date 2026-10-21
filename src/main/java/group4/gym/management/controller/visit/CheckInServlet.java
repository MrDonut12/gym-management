package group4.gym.management.controller.visit;

import group4.gym.management.service.MemberService;
import group4.gym.management.service.VisitService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Member check-in (CN-13, CN-14). Manager, Receptionist.
 */
@WebServlet(name = "CheckInServlet", urlPatterns = {"/visits/check-in"})
public class CheckInServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/visit/check-in.jsp";

    private final MemberService memberService = new MemberService();
    private final VisitService visitService = new VisitService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: visitService.autoCloseOverdueVisits() (catch-up close step).
        //       If a keyword is given -> memberService.search so a member can be chosen; set the results on the request.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read memberId; visitService.checkIn(currentAccount, memberId).
        //       Catch BusinessRuleException and show the specific rejection reason (GT-02): plan expired, soft-deleted, 20:00 or later / before 07:00, already OPEN, not PAID yet...
        // Output: success: forward to VIEW with a message; failure: forward to VIEW with an error.
    }
}
