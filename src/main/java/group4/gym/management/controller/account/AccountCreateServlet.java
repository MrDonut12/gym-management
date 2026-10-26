package group4.gym.management.controller.account;

import group4.gym.management.service.account.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create a staff account (CN-06). ADMIN only.
 */
@WebServlet(name = "AccountCreateServlet", urlPatterns = {"/admin/accounts/create"})
public class AccountCreateServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/account/form.jsp";

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Set mode = create and roles = Role.values() on the request.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: username, password, confirmPassword via getRawString; fullName, phone, email via getString; role via getEnum.
        //       accountService.createAccount(sessionUser, account, password, confirmPassword); catch ValidationException.
        // Output: success: flash + redirect to /admin/accounts; failure: forward to VIEW keeping the values (except passwords) with fieldErrors.
    }
}
