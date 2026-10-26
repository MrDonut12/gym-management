package group4.gym.management.controller.member;

import group4.gym.management.service.member.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Update a member profile that is not soft-deleted (CN-14, QT-11). MANAGER, RECEPTIONIST.
 */
@WebServlet(name = "MemberEditServlet", urlPatterns = {"/staff/members/edit"})
public class MemberEditServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/member/form.jsp";

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: id = getPositiveId (null / NotFoundException -> 404); memberService.getForEdit;
        //       BusinessRuleException (soft-deleted) -> flash error + redirect to the detail page; set member, mode = edit, genders.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id, fullName, phone, email, dateOfBirth, gender; memberService.updateMember(sessionUser, member).
        // Output: success: flash + redirect to /staff/members/detail?id=<id>; failure: forward to VIEW with errors.
    }
}
