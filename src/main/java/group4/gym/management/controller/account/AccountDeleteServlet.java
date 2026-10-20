package group4.gym.management.controller.account;

import group4.gym.management.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Soft-delete an account (CN-04) - Administrator only, self-deletion is forbidden. POST only.
 */
@WebServlet(name = "AccountDeleteServlet", urlPatterns = {"/accounts/delete"})
public class AccountDeleteServlet extends HttpServlet {

    private final AccountService accountService = new AccountService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id; accountService.softDeleteAccount(currentAccount, id).
        // Output: redirect to /accounts with a message; 403 if the actor lacks permission.
    }
}
