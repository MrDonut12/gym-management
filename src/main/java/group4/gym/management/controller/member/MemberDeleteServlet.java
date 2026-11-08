package group4.gym.management.controller.member;

import group4.gym.management.exception.AccessDeniedException;
import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.exception.NotFoundException;
import group4.gym.management.service.member.MemberService;
import group4.gym.management.util.RequestUtil;
import group4.gym.management.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Soft-delete a member, after a confirmation dialog. MANAGER only, POST only.
 */
@WebServlet(name = "MemberDeleteServlet", urlPatterns = {"/manager/members/delete"})
public class MemberDeleteServlet extends HttpServlet {

    private final MemberService memberService = new MemberService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            memberService.softDelete(SessionUtil.getSessionUser(request), id);
            SessionUtil.flashMessage(request, "Member profile deleted. Pending registrations were cancelled.");
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (AccessDeniedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        } catch (BusinessRuleException ex) {
            SessionUtil.flashError(request, ex.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/staff/members/detail?id=" + id);
    }
}
