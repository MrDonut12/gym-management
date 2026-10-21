package group4.gym.management.controller.member;

import group4.gym.management.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create a member profile (CN-06). Manager, Receptionist.
 */
@WebServlet(name = "MemberCreateServlet", urlPatterns = {"/members/create"})
public class MemberCreateServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/member/form.jsp";

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Set mode = create on the request.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read fullName, phone, email, dateOfBirth, gender -> Member.
        //       memberService.createMember; duplicate phone -> message asking to check the existing profile.
        // Output: success: redirect to /members/detail?id=<new id>; failure: forward to VIEW keeping the data with errors.
    }
}
