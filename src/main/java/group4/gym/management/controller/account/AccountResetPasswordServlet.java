package group4.gym.management.controller.account;

import group4.gym.management.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Administrator resets another account's password (CN-36).
 */
@WebServlet(name = "AccountResetPasswordServlet", urlPatterns = {"/accounts/reset-password"})
public class AccountResetPasswordServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/account/reset-password.jsp";

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id; accountService.getManageableAccount; set account on the request.
        // Output: forward to VIEW; 403 if the actor is not an Administrator.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id, newPassword, confirmPassword; accountService.resetPassword.
        // Output: success: redirect to /accounts; failure: forward to VIEW with errors.
    }
}
