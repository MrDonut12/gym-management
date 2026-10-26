package group4.gym.management.controller.account;

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
        // TODO: Read id and action (lock | unlock); accountService.setLocked(sessionUser, id, "lock".equals(action)).
        //       BusinessRuleException -> flash error.
        // Output: redirect to /admin/accounts with a flash message.
    }
}
