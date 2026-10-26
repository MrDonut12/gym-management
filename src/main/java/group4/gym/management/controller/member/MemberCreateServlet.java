package group4.gym.management.controller.member;

import group4.gym.management.service.member.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create a member profile (CN-12). MANAGER, RECEPTIONIST.
 */
@WebServlet(name = "MemberCreateServlet", urlPatterns = {"/staff/members/create"})
public class MemberCreateServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/member/form.jsp";

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Set mode = create and genders = Gender.values() on the request.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read fullName, phone, email, dateOfBirth (invalid date -> field error, HL-05), gender -> Member.
        //       memberService.createMember(sessionUser, member); ValidationException -> forward with fieldErrors
        //       (a duplicate phone error contains the code of the profile using it).
        // Output: success: flash + redirect to /staff/members/detail?id=<new id>; failure: forward to VIEW keeping the values.
    }
}
