package group4.gym.management.controller.member;

import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.exception.NotFoundException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.member.Member;
import group4.gym.management.service.member.MemberService;
import group4.gym.management.util.RequestUtil;
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
 * Update a member profile that is not soft-deleted (FR-14, BR-11). MANAGER, RECEPTIONIST.
 */
@WebServlet(name = "MemberEditServlet", urlPatterns = {"/staff/members/edit"})
public class MemberEditServlet extends HttpServlet {

    private static final String MODE = "edit";

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Member member;
        try {
            member = memberService.getForEdit(id);
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (BusinessRuleException ex) {
            redirectToDetail(request, response, id, ex.getMessage());
            return;
        }
        MemberForm.forward(request, response, MODE, member);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        Member member = MemberForm.read(request, fieldErrors);
        member.setId(id);
        if (!fieldErrors.isEmpty()) {
            MemberForm.forwardWithErrors(request, response, MODE, member, fieldErrors);
            return;
        }

        try {
            memberService.updateMember(SessionUtil.getSessionUser(request), member);
            SessionUtil.flashMessage(request, "Member profile saved.");
            response.sendRedirect(request.getContextPath() + "/staff/members/detail?id=" + id);
        } catch (ValidationException ex) {
            MemberForm.forwardWithErrors(request, response, MODE, member, ex.getFieldErrors());
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (BusinessRuleException ex) {
            redirectToDetail(request, response, id, ex.getMessage());
        }
    }

    /**
     * A soft-deleted profile cannot be edited (BR-11): show the reason on the detail page.
     */
    private void redirectToDetail(HttpServletRequest request, HttpServletResponse response,
            int id, String error) throws IOException {
        SessionUtil.flashError(request, error);
        response.sendRedirect(request.getContextPath() + "/staff/members/detail?id=" + id);
    }
}
