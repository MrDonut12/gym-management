package group4.gym.management.controller.account;

import group4.gym.management.service.account.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Edit full name, phone, email and role (CN-07). The username is read-only; an ADMIN cannot change their own role (QT-14).
 * ADMIN only.
 */
@WebServlet(name = "AccountEditServlet", urlPatterns = {"/admin/accounts/edit"})
public class AccountEditServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/account/form.jsp";

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: id = RequestUtil.getPositiveId; null or NotFoundException -> 404 (HL-17);
        //       accountService.getById; set account, mode = edit, roles on the request.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id, fullName, phone, email, role (any username parameter is ignored); accountService.updateAccount(sessionUser, account).
        //       ValidationException -> forward with fieldErrors; BusinessRuleException (QT-14) -> forward with error.
        // Output: success: flash + redirect to /admin/accounts; failure: forward to VIEW.
    }
}
