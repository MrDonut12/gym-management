package group4.gym.management.controller.member;

import group4.gym.management.exception.AccessDeniedException;
import group4.gym.management.exception.NotFoundException;
import group4.gym.management.service.member.MemberService;
import group4.gym.management.util.RequestUtil;
import group4.gym.management.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Member detail (FR-13): profile, soft-delete label, status today, expiry warning, registration history.
 * MANAGER, RECEPTIONIST (a soft-deleted profile: MANAGER only, BR-11).
 */
@WebServlet(name = "MemberDetailServlet", urlPatterns = {"/staff/members/detail"})
public class MemberDetailServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/member/detail.jsp";

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            request.setAttribute("detail", memberService.getDetail(SessionUtil.getSessionUser(request), id));
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (AccessDeniedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        request.getRequestDispatcher(VIEW).forward(request, response);
    }
}
