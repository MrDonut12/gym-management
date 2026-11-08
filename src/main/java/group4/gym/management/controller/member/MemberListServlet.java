package group4.gym.management.controller.member;

import group4.gym.management.exception.AccessDeniedException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.member.MemberScope;
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
 * Member list and search (FR-11), the RECEPTIONIST home page. MANAGER, RECEPTIONIST.
 * One search box (MemberKeyword): code HV + 6 digits (exact), phone (exact) or full name (partial); anything else
 * matches nothing (AC-09).
 * Scope DELETED/ALL is MANAGER only (checked in MemberService, 403 otherwise).
 */
@WebServlet(name = "MemberListServlet", urlPatterns = {"/staff/members"})
public class MemberListServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/member/list.jsp";

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = RequestUtil.getString(request, "keyword");
        MemberScope scope = RequestUtil.getEnum(request, "scope", MemberScope.class);
        if (scope == null) {
            scope = MemberScope.ACTIVE;
        }
        int page = RequestUtil.getPage(request);

        request.setAttribute("keyword", keyword);
        request.setAttribute("scope", scope);
        try {
            request.setAttribute("page", memberService.search(
                    SessionUtil.getSessionUser(request), keyword, scope, page));
        } catch (AccessDeniedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        } catch (ValidationException ex) {
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("fieldErrors", ex.getFieldErrors());
        }
        request.getRequestDispatcher(VIEW).forward(request, response);
    }
}
