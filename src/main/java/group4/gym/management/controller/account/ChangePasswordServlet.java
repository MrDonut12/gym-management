package group4.gym.management.controller.account;

import group4.gym.management.service.account.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Change own password (CN-09). Every role; the current session is kept.
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/change-password"})
public class ChangePasswordServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/account/change-password.jsp";

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Show the form.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: currentPassword, newPassword, confirmPassword via RequestUtil.getRawString;
        //       accountService.changeOwnPassword(sessionUser, ...); catch ValidationException/BusinessRuleException.
        // Output: success: flash message + redirect to /change-password (CL-07); failure: forward to VIEW with errors (no password re-filled).
    }
}
