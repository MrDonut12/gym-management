package group4.gym.management.controller.auth;

import group4.gym.management.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Change own password (CN-36) - every role after valid authentication.
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/account/change-password"})
public class ChangePasswordServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/auth/change-password.jsp";

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Show the change-password form.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read currentPassword, newPassword, confirmPassword.
        //       accountService.changeOwnPassword(currentAccount, ...); catch ValidationException/BusinessRuleException.
        // Output: success: forward to VIEW with a message; failure: forward to VIEW with error/fieldErrors.
    }
}
