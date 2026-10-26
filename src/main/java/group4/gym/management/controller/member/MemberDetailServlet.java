package group4.gym.management.controller.member;

import group4.gym.management.service.member.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Member detail (CN-13): profile, soft-delete label, status today, expiry warning, registration history.
 * MANAGER, RECEPTIONIST (a soft-deleted profile: MANAGER only, QT-11).
 */
@WebServlet(name = "MemberDetailServlet", urlPatterns = {"/staff/members/detail"})
public class MemberDetailServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/member/detail.jsp";

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: id = getPositiveId; memberService.getDetail(sessionUser, id);
        //       null id / NotFoundException -> 404; AccessDeniedException -> 403; set detail on the request.
        // Output: forward to VIEW.
    }
}
