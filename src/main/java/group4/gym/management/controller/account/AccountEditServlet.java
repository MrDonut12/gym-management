package group4.gym.management.controller.account;

import group4.gym.management.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Edit staff information (CN-35) and change role (Administrator only, CN-04).
 */
@WebServlet(name = "AccountEditServlet", urlPatterns = {"/accounts/edit"})
public class AccountEditServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/account/form.jsp";

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id; accountService.getManageableAccount(currentAccount, id); set account and mode = edit on the request.
        // Output: forward to VIEW; 403 if outside the management scope.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id, fullName, phone, email, role.
        //       accountService.updateAccount; if the role changed -> accountService.changeRole.
        //       The password is never displayed again and is not edited in this form.
        // Output: success: redirect to /accounts; failure: forward to VIEW with errors.
    }
}
