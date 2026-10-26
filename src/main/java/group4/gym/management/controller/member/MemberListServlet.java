package group4.gym.management.controller.member;

import group4.gym.management.service.member.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Member list and search (CN-11), the RECEPTIONIST home page. MANAGER, RECEPTIONIST.
 * Scope DELETED/ALL is MANAGER only (checked in MemberService, 403 otherwise).
 */
@WebServlet(name = "MemberListServlet", urlPatterns = {"/staff/members"})
public class MemberListServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/member/list.jsp";

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read keyword, scope (MemberScope, default ACTIVE), page (RequestUtil.getPage);
        //       memberService.search(sessionUser, keyword, scope, page); AccessDeniedException -> 403.
        //       Set page (Page<MemberListItem>), keyword, scope on the request; pagination links keep keyword and scope (KT-09).
        // Output: forward to VIEW.
    }
}
