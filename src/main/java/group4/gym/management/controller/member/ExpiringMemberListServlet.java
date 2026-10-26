package group4.gym.management.controller.member;

import group4.gym.management.service.member.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Expiring-soon list (CN-17, QT-08). Display only; no notification is sent. MANAGER, RECEPTIONIST.
 */
@WebServlet(name = "ExpiringMemberListServlet", urlPatterns = {"/staff/members/expiring"})
public class ExpiringMemberListServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/member/expiring.jsp";

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: memberService.listExpiringSoon(); set members on the request.
        // Output: forward to VIEW.
    }
}
