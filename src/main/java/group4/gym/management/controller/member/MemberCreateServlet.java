package group4.gym.management.controller.member;

import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.member.Member;
import group4.gym.management.service.member.MemberService;
import group4.gym.management.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Create a member profile. MANAGER, RECEPTIONIST.
 */
@WebServlet(name = "MemberCreateServlet", urlPatterns = {"/staff/members/create"})
public class MemberCreateServlet extends HttpServlet {

    private static final String MODE = "create";

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MemberForm.forward(request, response, MODE, new Member());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        Member member = MemberForm.read(request, fieldErrors);
        if (!fieldErrors.isEmpty()) {
            MemberForm.forwardWithErrors(request, response, MODE, member, fieldErrors);
            return;
        }

        try {
            int id = memberService.createMember(SessionUtil.getSessionUser(request), member);
            SessionUtil.flashMessage(request, "Member profile created.");
            response.sendRedirect(request.getContextPath() + "/staff/members/detail?id=" + id);
        } catch (ValidationException ex) {
            MemberForm.forwardWithErrors(request, response, MODE, member, ex.getFieldErrors());
        }
    }
}
