package group4.gym.management.controller.member;

import group4.gym.management.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * List / search members by code, name or phone (CN-07). Owner, Manager, Receptionist.
 */
@WebServlet(name = "MemberListServlet", urlPatterns = {"/members"})
public class MemberListServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/member/list.jsp";

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read keyword and the profile status filter (all/active/deleted).
        //       memberService.search(keyword, deletedFilter); put the list and the filter on the request.
        // Output: forward to VIEW.
    }
}
