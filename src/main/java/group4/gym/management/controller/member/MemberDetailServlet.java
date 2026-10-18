package group4.gym.management.controller.member;

import group4.gym.management.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Member detail: profile status, status of each plan registration, expiry warning, OPEN visit (CN-12).
 */
@WebServlet(name = "MemberDetailServlet", urlPatterns = {"/members/detail"})
public class MemberDetailServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/member/detail.jsp";

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id; memberService.getMemberStatus(id) (includes the catch-up close step, CN-15 (d)); set the view on the request.
        // Output: forward to VIEW; 404 if the member does not exist.
    }
}
