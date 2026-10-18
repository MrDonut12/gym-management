package group4.gym.management.controller.account;

import group4.gym.management.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Lock / unlock an account (CN-04). POST only.
 */
@WebServlet(name = "AccountLockServlet", urlPatterns = {"/accounts/lock"})
public class AccountLockServlet extends HttpServlet {

    private final AccountService accountService = new AccountService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id and action (lock | unlock).
        //       lock -> accountService.lockAccount; unlock -> accountService.unlockAccount (does not restore sessions).
        // Output: redirect to /accounts with a message; 403 if the actor lacks permission.
    }
}
