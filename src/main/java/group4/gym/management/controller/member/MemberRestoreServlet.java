package group4.gym.management.controller.member;

import group4.gym.management.service.member.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Restore a soft-deleted member (CN-16, QT-10). MANAGER only, POST only.
 */
@WebServlet(name = "MemberRestoreServlet", urlPatterns = {"/manager/members/restore"})
public class MemberRestoreServlet extends HttpServlet {

    private final MemberService memberService = new MemberService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: id = getPositiveId (null / NotFoundException -> 404); memberService.restore(sessionUser, id).
        // Output: redirect to /staff/members/detail?id=<id> with a flash message.
    }
}
