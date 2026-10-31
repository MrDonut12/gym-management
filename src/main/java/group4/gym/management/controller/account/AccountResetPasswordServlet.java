package group4.gym.management.controller.account;

import group4.gym.management.util.RequestUtil;
import group4.gym.management.util.SessionUtil;
import group4.gym.management.exception.ValidationException;
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
 * ADMIN sets a new password for another account (CN-10). Not for oneself (QT-14); the lock status is unchanged.
 */
@WebServlet(name = "AccountResetPasswordServlet", urlPatterns = {"/admin/accounts/reset-password"})
public class AccountResetPasswordServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/account/reset-password.jsp";

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (id == SessionUtil.getSessionUser(request).getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try {
            request.setAttribute("account", accountService.getById(id));
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            accountService.resetPassword(SessionUtil.getSessionUser(request), id,
                    RequestUtil.getRawString(request, "newPassword"), RequestUtil.getRawString(request, "confirmPassword"));
            SessionUtil.flashMessage(request, "Password reset; account status unchanged.");
            response.sendRedirect(request.getContextPath() + "/admin/accounts");
            return;
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (ValidationException ex) {
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("fieldErrors", ex.getFieldErrors());
        } catch (BusinessRuleException ex) {
            request.setAttribute("error", ex.getMessage());
        }
        doGet(request, response);
    }
}
