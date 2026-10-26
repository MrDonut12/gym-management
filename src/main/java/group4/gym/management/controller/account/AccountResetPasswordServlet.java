package group4.gym.management.controller.account;

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
        // TODO: id = getPositiveId (null / NotFoundException -> 404); accountService.getById; set account on the request.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id, newPassword, confirmPassword (getRawString); accountService.resetPassword(sessionUser, id, ...).
        // Output: success: flash + redirect to /admin/accounts; failure: forward to VIEW with errors.
    }
}
