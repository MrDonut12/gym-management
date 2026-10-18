package group4.gym.management.controller.account;

import group4.gym.management.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * List of staff accounts within the viewer's management scope (CN-04, CN-35). Administrator, Manager.
 */
@WebServlet(name = "AccountListServlet", urlPatterns = {"/accounts"})
public class AccountListServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/account/list.jsp";

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read the filters keyword, role, status, includeDeleted.
        //       accountService.listManageableAccounts(currentAccount, ...); put the list and the filters on the request.
        // Output: forward to VIEW.
    }
}
