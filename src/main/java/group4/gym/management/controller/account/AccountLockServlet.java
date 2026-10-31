package group4.gym.management.controller.account;

import group4.gym.management.model.account.Account;
import group4.gym.management.util.RequestUtil;
import group4.gym.management.util.SessionUtil;
import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.exception.NotFoundException;
import group4.gym.management.service.account.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Lock / unlock an account (CN-08), after a confirmation dialog on the list. An ADMIN cannot lock themselves (QT-14).
 * ADMIN only, POST only.
 */
@WebServlet(name = "AccountLockServlet", urlPatterns = {"/admin/accounts/lock"})
public class AccountLockServlet extends HttpServlet {

    private final AccountService accountService = new AccountService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String action = request.getParameter("action");
        if (!"lock".equals(action) && !"unlock".equals(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            accountService.setLocked(SessionUtil.getSessionUser(request), id, "lock".equals(action));
            SessionUtil.flashMessage(request, "Account status updated.");
        } catch (BusinessRuleException ex) {
            SessionUtil.flashError(request, ex.getMessage());
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/admin/accounts");
    }
}
