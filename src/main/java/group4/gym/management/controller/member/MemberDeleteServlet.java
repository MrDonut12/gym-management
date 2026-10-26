package group4.gym.management.controller.member;

import group4.gym.management.service.member.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Soft-delete a member (CN-15, QT-06, QT-09), after a confirmation dialog. MANAGER only, POST only.
 */
@WebServlet(name = "MemberDeleteServlet", urlPatterns = {"/manager/members/delete"})
public class MemberDeleteServlet extends HttpServlet {

    private final MemberService memberService = new MemberService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: id = getPositiveId (null / NotFoundException -> 404); memberService.softDelete(sessionUser, id).
        //       BusinessRuleException (unexpired PAID) -> flash error with the reason (KT-11).
        // Output: redirect to /staff/members/detail?id=<id> with a flash message.
    }
}
