package group4.gym.management.controller.member;

import group4.gym.management.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Update a member profile (CN-08). Manager, Receptionist.
 */
@WebServlet(name = "MemberEditServlet", urlPatterns = {"/members/edit"})
public class MemberEditServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/member/form.jsp";

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id; memberService.getById; a soft-deleted profile cannot be edited; set member, mode = edit.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id, fullName, phone, email, dateOfBirth, gender; memberService.updateMember.
        // Output: success: redirect to /members/detail?id=<id>; failure: forward to VIEW with errors.
    }
}
